package br.com.jkavdev.fullcycle.subscription.application.subscription.impl;

import br.com.jkavdev.fullcycle.subscription.application.subscription.ChargeSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.account.Account;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.payment.BillingAddress;
import br.com.jkavdev.fullcycle.subscription.domain.payment.Payment;
import br.com.jkavdev.fullcycle.subscription.domain.payment.PaymentGateway;
import br.com.jkavdev.fullcycle.subscription.domain.payment.Transaction;
import br.com.jkavdev.fullcycle.subscription.domain.plan.Plan;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionCommand;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionCommand.IncompleteSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionCommand.RenewSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
import br.com.jkavdev.fullcycle.subscription.domain.utils.IdUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DefaultChargeSubscription extends ChargeSubscription {

    private static final int MAX_INCOMPLETE_DAYS = 2;

    private final AccountGateway accountGateway;

    private final Clock clock;

    private final PaymentGateway paymentGateway;

    private final PlanGateway planGateway;

    private final SubscriptionGateway subscriptionGateway;

    public DefaultChargeSubscription(
            final AccountGateway accountGateway,
            final Clock clock,
            final PaymentGateway paymentGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.clock = Objects.requireNonNull(clock);
        this.paymentGateway = Objects.requireNonNull(paymentGateway);
        this.planGateway = Objects.requireNonNull(planGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
    }

    @Override
    public Output execute(final Input input) {
        if (input == null) {
            throw new IllegalArgumentException("input to DefaultChargeSubscription cannot be null");
        }
        final var anAccountId = new AccountId(input.accountId());
        final var anSubscriptionId = new SubscriptionId(input.subscriptionId());
        final var aSubscription = subscriptionGateway.subscriptionOfId(anSubscriptionId)
                .filter(it -> it.accountId().equals(anAccountId))
                .orElseThrow(() -> DomainException.notFound(Subscription.class, anSubscriptionId));
        final var now = clock.instant();

        if (aSubscription.dueDate().isAfter(LocalDate.ofInstant(now, ZoneId.systemDefault()))) {
            return new StdOuput(
                    anSubscriptionId,
                    aSubscription.status().value(),
                    aSubscription.dueDate(),
                    null
            );
        }

        final var aPlan = planGateway.planOfId(aSubscription.planId())
                .orElseThrow(() -> DomainException.notFound(Subscription.class, aSubscription.planId()));

        final var anUserAccount = accountGateway.accountOfId(anAccountId)
                .orElseThrow(() -> DomainException.notFound(Account.class, anAccountId));

        final var aPayment = newPaymentWith(input, aPlan, anUserAccount);
        final var actualTransaction = paymentGateway.processPayment(aPayment);

        if (actualTransaction.isSuccess()) {
            aSubscription.execute(new RenewSubscription(aPlan, actualTransaction.transactionId()));
        } else if (hasTolerableDays(aSubscription.dueDate(), now)) {
            aSubscription.execute(new IncompleteSubscription(
                    actualTransaction.errorMessage(),
                    actualTransaction.transactionId()
            ));
        } else {
            aSubscription.execute(new SubscriptionCommand.CancelSubscription());
        }

        subscriptionGateway.save(aSubscription);

        return new StdOuput(
                anSubscriptionId,
                aSubscription.status().value(),
                aSubscription.dueDate(),
                actualTransaction
        );
    }

    private boolean hasTolerableDays(final LocalDate dueDate, final Instant now) {
        return ChronoUnit.DAYS.between(dueDate, LocalDate.ofInstant(now, ZoneId.systemDefault())) <= MAX_INCOMPLETE_DAYS;
    }

    private Payment newPaymentWith(
            final Input input,
            final Plan aPlan,
            final Account anUserAccount
    ) {
        return Payment.create(
                input.paymentType(),
                IdUtils.uniqueId(),
                aPlan.price().amount(),
                new BillingAddress(
                        anUserAccount.billingAddress().zipcode(),
                        anUserAccount.billingAddress().number(),
                        anUserAccount.billingAddress().complement(),
                        anUserAccount.billingAddress().country()
                ),
                input.creditCardToken()
        );
    }

    public record StdOuput(
            SubscriptionId subscriptionId,
            String subscriptionStatus,
            LocalDate subscriptionDueDate,
            Transaction paymentTransaction
    ) implements ChargeSubscription.Output {

    }

}
