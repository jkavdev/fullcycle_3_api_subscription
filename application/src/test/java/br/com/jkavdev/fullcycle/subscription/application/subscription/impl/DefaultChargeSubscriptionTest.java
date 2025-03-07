package br.com.jkavdev.fullcycle.subscription.application.subscription.impl;

import br.com.jkavdev.fullcycle.subscription.application.subscription.ChargeSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.UnitTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.payment.Payment;
import br.com.jkavdev.fullcycle.subscription.domain.payment.PaymentGateway;
import br.com.jkavdev.fullcycle.subscription.domain.payment.PixPayment;
import br.com.jkavdev.fullcycle.subscription.domain.payment.Transaction;
import br.com.jkavdev.fullcycle.subscription.domain.plan.Plan;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.status.SubscriptionStatus;
import br.com.jkavdev.fullcycle.subscription.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

class DefaultChargeSubscriptionTest extends UnitTest {

    @InjectMocks
    private DefaultChargeSubscription target;

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private Clock clock;

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private PlanGateway planGateway;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @Captor
    ArgumentCaptor<Payment> paymentCaptor;

    @Test
    public void givenSubscriptionOutOfChargePeriod_whenCallsChargeSubscription_shouldSkipCharges() {
        // given
        final var referenceDate = LocalDateTime.now().plusDays(2);
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedAccount = Fixture.Accounts.jhou();
        final var expectedStatus = SubscriptionStatus.ACTIVE;
        final var expectedDueDate = referenceDate.toLocalDate();
        final var expectedSubscription = newSubscriptionWith(
                expectedAccount.id(),
                expectedPlan,
                expectedStatus,
                referenceDate
        );

        Mockito.when(clock.instant())
                .thenReturn(InstantUtils.now());
        Mockito.when(subscriptionGateway.latestSubscriptionOfAccount(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedSubscription));

        // when
        final var actualOutput = target.execute(
                new ChargeSubscriptionTestInput(
                        expectedAccount.id().value(),
                        expectedSubscription.id().value(),
                        Payment.PIX,
                        null
                )
        );

        // then
        Assertions.assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        Assertions.assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        Assertions.assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        Assertions.assertNull(actualOutput.paymentTransaction());

        Mockito.verify(subscriptionGateway, Mockito.times(1))
                .latestSubscriptionOfAccount(expectedAccount.id());
        Mockito.verify(planGateway, Mockito.never())
                .planOfId(ArgumentMatchers.any());
        Mockito.verify(accountGateway, Mockito.never())
                .accountOfId(ArgumentMatchers.any());
        Mockito.verify(paymentGateway, Mockito.never())
                .processPayment(ArgumentMatchers.any());

    }

    @Test
    public void givenSubscriptionWithPastDueDate_whenChargeSuccessfully_shouldSaveAsActive() {
        // given
        final var referenceDate = LocalDateTime.now().minusDays(2);
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedAccount = Fixture.Accounts.jhou();
        final var expectedStatus = SubscriptionStatus.ACTIVE;
        final var expectedDueDate = referenceDate.toLocalDate().plusMonths(1);
        final var expectedSubscription = newSubscriptionWith(
                expectedAccount.id(),
                expectedPlan,
                SubscriptionStatus.INCOMPLETE,
                referenceDate
        );
        final var expecteTransaction = Transaction.success("tra-123");

        Mockito.when(clock.instant())
                .thenReturn(InstantUtils.now());
        Mockito.when(subscriptionGateway.latestSubscriptionOfAccount(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedSubscription));
        Mockito.when(planGateway.planOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedPlan));
        Mockito.when(accountGateway.accountOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedAccount));
        Mockito.when(paymentGateway.processPayment(ArgumentMatchers.any()))
                .thenReturn(expecteTransaction);

        // when
        final var actualOutput = target.execute(
                new ChargeSubscriptionTestInput(
                        expectedAccount.id().value(),
                        expectedSubscription.id().value(),
                        Payment.PIX,
                        null
                )
        );

        // then
        Assertions.assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        Assertions.assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        Assertions.assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        Assertions.assertEquals(expecteTransaction, actualOutput.paymentTransaction());

        Mockito.verify(subscriptionGateway, Mockito.times(1))
                .latestSubscriptionOfAccount(expectedAccount.id());
        Mockito.verify(planGateway, Mockito.times(1))
                .planOfId(expectedPlan.id());
        Mockito.verify(accountGateway, Mockito.times(1))
                .accountOfId(expectedAccount.id());
        Mockito.verify(paymentGateway, Mockito.times(1))
                .processPayment(paymentCaptor.capture());

        final var actualPayment = paymentCaptor.getValue();
        Assertions.assertInstanceOf(PixPayment.class, actualPayment);
        Assertions.assertNotNull(actualPayment.orderId());
        Assertions.assertEquals(expectedPlan.price().amount(), actualPayment.amount());

    }

    @Test
    public void givenSubscriptionWithDueDateNow_whenChargeSuccessfully_shouldSaveAsActive() {
        // given
        final var referenceDate = LocalDateTime.now();
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedAccount = Fixture.Accounts.jhou();
        final var expectedStatus = SubscriptionStatus.ACTIVE;
        final var expectedDueDate = referenceDate.toLocalDate().plusMonths(1);
        final var expectedSubscription = newSubscriptionWith(
                expectedAccount.id(),
                expectedPlan,
                SubscriptionStatus.ACTIVE,
                referenceDate
        );
        final var expecteTransaction = Transaction.success("tra-123");

        Mockito.when(clock.instant())
                .thenReturn(InstantUtils.now());
        Mockito.when(subscriptionGateway.latestSubscriptionOfAccount(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedSubscription));
        Mockito.when(planGateway.planOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedPlan));
        Mockito.when(accountGateway.accountOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedAccount));
        Mockito.when(paymentGateway.processPayment(ArgumentMatchers.any()))
                .thenReturn(expecteTransaction);

        // when
        final var actualOutput = target.execute(
                new ChargeSubscriptionTestInput(
                        expectedAccount.id().value(),
                        expectedSubscription.id().value(),
                        Payment.PIX,
                        null
                )
        );

        // then
        Assertions.assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        Assertions.assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        Assertions.assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        Assertions.assertEquals(expecteTransaction, actualOutput.paymentTransaction());

        Mockito.verify(subscriptionGateway, Mockito.times(1))
                .latestSubscriptionOfAccount(expectedAccount.id());
        Mockito.verify(planGateway, Mockito.times(1))
                .planOfId(expectedPlan.id());
        Mockito.verify(accountGateway, Mockito.times(1))
                .accountOfId(expectedAccount.id());
        Mockito.verify(paymentGateway, Mockito.times(1))
                .processPayment(paymentCaptor.capture());

        final var actualPayment = paymentCaptor.getValue();
        Assertions.assertInstanceOf(PixPayment.class, actualPayment);
        Assertions.assertNotNull(actualPayment.orderId());
        Assertions.assertEquals(expectedPlan.price().amount(), actualPayment.amount());

    }

    @Test
    public void givenSubscriptionWithDueDateNow_whenChargeFailure_shouldSaveAsIncomplete() {
        // given
        final var referenceDate = LocalDateTime.now();
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedAccount = Fixture.Accounts.jhou();
        final var expectedStatus = SubscriptionStatus.INCOMPLETE;
        final var expectedDueDate = referenceDate.toLocalDate();
        final var expectedSubscription = newSubscriptionWith(
                expectedAccount.id(),
                expectedPlan,
                SubscriptionStatus.ACTIVE,
                referenceDate
        );
        final var expecteTransaction = Transaction.failure("tra-123", "deu erro porra....");

        Mockito.when(clock.instant())
                .thenReturn(InstantUtils.now());
        Mockito.when(subscriptionGateway.latestSubscriptionOfAccount(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedSubscription));
        Mockito.when(planGateway.planOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedPlan));
        Mockito.when(accountGateway.accountOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedAccount));
        Mockito.when(paymentGateway.processPayment(ArgumentMatchers.any()))
                .thenReturn(expecteTransaction);

        // when
        final var actualOutput = target.execute(
                new ChargeSubscriptionTestInput(
                        expectedAccount.id().value(),
                        expectedSubscription.id().value(),
                        Payment.PIX,
                        null
                )
        );

        // then
        Assertions.assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        Assertions.assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        Assertions.assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        Assertions.assertEquals(expecteTransaction, actualOutput.paymentTransaction());

        Mockito.verify(subscriptionGateway, Mockito.times(1))
                .latestSubscriptionOfAccount(expectedAccount.id());
        Mockito.verify(planGateway, Mockito.times(1))
                .planOfId(expectedPlan.id());
        Mockito.verify(accountGateway, Mockito.times(1))
                .accountOfId(expectedAccount.id());
        Mockito.verify(paymentGateway, Mockito.times(1))
                .processPayment(paymentCaptor.capture());

        final var actualPayment = paymentCaptor.getValue();
        Assertions.assertInstanceOf(PixPayment.class, actualPayment);
        Assertions.assertNotNull(actualPayment.orderId());
        Assertions.assertEquals(expectedPlan.price().amount(), actualPayment.amount());

    }

    @Test
    public void givenSubscriptionWithPastDueDate_whenChargeFailureAndPastMaxIncompleteDays_shouldSaveAsCanceled() {
        // given
        final var referenceDate = LocalDateTime.now().minusDays(4);
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedAccount = Fixture.Accounts.jhou();
        final var expectedStatus = SubscriptionStatus.CANCELED;
        final var expectedDueDate = referenceDate.toLocalDate();
        final var expectedSubscription = newSubscriptionWith(
                expectedAccount.id(),
                expectedPlan,
                SubscriptionStatus.INCOMPLETE,
                referenceDate
        );
        final var expecteTransaction = Transaction.failure("tra-123", "deu erro porra....");

        Mockito.when(clock.instant())
                .thenReturn(InstantUtils.now());
        Mockito.when(subscriptionGateway.latestSubscriptionOfAccount(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedSubscription));
        Mockito.when(planGateway.planOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedPlan));
        Mockito.when(accountGateway.accountOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedAccount));
        Mockito.when(paymentGateway.processPayment(ArgumentMatchers.any()))
                .thenReturn(expecteTransaction);

        // when
        final var actualOutput = target.execute(
                new ChargeSubscriptionTestInput(
                        expectedAccount.id().value(),
                        expectedSubscription.id().value(),
                        Payment.PIX,
                        null
                )
        );

        // then
        Assertions.assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        Assertions.assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        Assertions.assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        Assertions.assertEquals(expecteTransaction, actualOutput.paymentTransaction());

        Mockito.verify(subscriptionGateway, Mockito.times(1))
                .latestSubscriptionOfAccount(expectedAccount.id());
        Mockito.verify(planGateway, Mockito.times(1))
                .planOfId(expectedPlan.id());
        Mockito.verify(accountGateway, Mockito.times(1))
                .accountOfId(expectedAccount.id());
        Mockito.verify(paymentGateway, Mockito.times(1))
                .processPayment(paymentCaptor.capture());

        final var actualPayment = paymentCaptor.getValue();
        Assertions.assertInstanceOf(PixPayment.class, actualPayment);
        Assertions.assertNotNull(actualPayment.orderId());
        Assertions.assertEquals(expectedPlan.price().amount(), actualPayment.amount());

    }

    private static Subscription newSubscriptionWith(
            final AccountId accountId,
            final Plan plan,
            final String status,
            final LocalDateTime date
    ) {
        final var instant = date.toInstant(ZoneOffset.UTC);
        return Subscription.with(
                new SubscriptionId("sub123"),
                1,
                accountId,
                plan.id(),
                date.toLocalDate(),
                status,
                instant,
                "a123",
                instant,
                instant
        );
    }

    record ChargeSubscriptionTestInput(
            String accountId,
            String subscriptionId,
            String paymentType,
            String creditCardToken
    ) implements ChargeSubscription.Input {
    }

}