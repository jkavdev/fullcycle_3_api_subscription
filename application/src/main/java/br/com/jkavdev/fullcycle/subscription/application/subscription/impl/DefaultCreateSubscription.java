package br.com.jkavdev.fullcycle.subscription.application.subscription.impl;

import br.com.jkavdev.fullcycle.subscription.application.subscription.CreateSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.account.Account;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.plan.Plan;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanGateway;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;

import java.util.Objects;

public class DefaultCreateSubscription extends CreateSubscription {

    private final AccountGateway accountGateway;

    private final PlanGateway planGateway;

    private final SubscriptionGateway subscriptionGateway;

    public DefaultCreateSubscription(
            final AccountGateway accountGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.planGateway = Objects.requireNonNull(planGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
    }

    @Override
    public Output execute(final Input input) {
        if (input == null) {
            throw new IllegalArgumentException("input to DefaultCreateSubscription cannot be null");
        }
        final var accountId = new AccountId(input.accountId());
        final var planId = new PlanId(input.planIn());

        validateActiveSubscription(input, accountId);

        final var aPlan = planGateway.planOfId(planId)
                .filter(Plan::active)
                .orElseThrow(() -> DomainException.notFound(Plan.class, planId));

        final var anUserAccount = accountGateway.accountOfId(accountId)
                .orElseThrow(() -> DomainException.notFound(Account.class, accountId));

        final var aNewSubscription = newSubscriptionWith(anUserAccount, aPlan);
        subscriptionGateway.save(aNewSubscription);

        return new StdOuput(aNewSubscription.id());
    }

    private Subscription newSubscriptionWith(final Account anUserAccount, final Plan aPlan) {
        return Subscription.newSubscription(
                subscriptionGateway.nextId(),
                anUserAccount.id(),
                aPlan
        );
    }

    private void validateActiveSubscription(final Input input, final AccountId accountId) {
        subscriptionGateway.latestSubscriptionOfAccount(accountId).ifPresent(
                sub -> {
                    if (!sub.isCanceled()) {
                        throw DomainException.with("account %s already has a active subscription".formatted(input.accountId()));
                    }
                }
        );
    }

    public record StdOuput(SubscriptionId subscriptionId) implements CreateSubscription.Output {

    }

}
