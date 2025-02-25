package br.com.jkavdev.fullcycle.subscription.domain.subscription;

import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionCommand.CancelSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionCommand.IncompleteSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionCommand.RenewSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.status.SubscriptionStatus;
import br.com.jkavdev.fullcycle.subscription.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * 1 - caminho feliz de um novo agregado
 * 2 - caminho feliz restauracao do agregado
 * 3 - caminho de validacao
 */
public class SubscriptionTest {

    @Test
    public void givenValidParams_whenCallsNew_shouldInstantiate() {
        // given
        final var expectedId = new SubscriptionId("qualquerId");
        final var expectedVersion = 0;
        final var expectedAccountId = new AccountId("qualquerAccountId");
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedStatus = SubscriptionStatus.TRAILING;
        final var expectedDuedate = LocalDate.now().plusMonths(1);
        final Instant expectedLastRenewDate = null;
        final String expectedLasttransactionId = null;
        final var expectedEvents = 1;

        // when
        final var actualSubscription =
                Subscription.newSubscription(expectedId, expectedAccountId, expectedPlan);

        // then
        Assertions.assertNotNull(actualSubscription);
        Assertions.assertEquals(expectedId, actualSubscription.id());
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedPlan.id(), actualSubscription.planId());
        Assertions.assertEquals(expectedDuedate, actualSubscription.dueDate());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertEquals(expectedLastRenewDate, actualSubscription.lastRenewDate());
        Assertions.assertEquals(expectedLasttransactionId, actualSubscription.lastTransactionId());
        Assertions.assertNotNull(actualSubscription.createdAt());
        Assertions.assertNotNull(actualSubscription.updatedAt());

        Assertions.assertEquals(expectedEvents, actualSubscription.domainEvents().size());
        Assertions.assertInstanceOf(SubscriptionCreated.class, actualSubscription.domainEvents().getFirst());
    }

    @Test
    public void givenValidParams_whenCallsWith_shouldInstantiate() {
        // given
        final var expectedId = new SubscriptionId("qualquerId");
        final var expectedVersion = 0;
        final var expectedAccountId = new AccountId("qualquerAccountId");
        final var expectedPlanId = new PlanId(123456L);
        final var expectedStatus = SubscriptionStatus.TRAILING;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDuedate = LocalDate.now().plusMonths(1);
        final var expectedLastRenewDate = InstantUtils.now().minus(1, ChronoUnit.DAYS);
        final var expectedLasttransactionId = UUID.randomUUID().toString();

        // when
        final var actualSubscription = Subscription.with(
                expectedId,
                expectedVersion,
                expectedAccountId,
                expectedPlanId,
                expectedDuedate,
                expectedStatus,
                expectedLastRenewDate,
                expectedLasttransactionId,
                expectedCreatedAt,
                expectedUpdatedAt
        );

        // then
        Assertions.assertNotNull(actualSubscription);
        Assertions.assertEquals(expectedId, actualSubscription.id());
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedPlanId, actualSubscription.planId());
        Assertions.assertEquals(expectedDuedate, actualSubscription.dueDate());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertEquals(expectedLastRenewDate, actualSubscription.lastRenewDate());
        Assertions.assertEquals(expectedLasttransactionId, actualSubscription.lastTransactionId());
        Assertions.assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualSubscription.updatedAt());

        Assertions.assertTrue(actualSubscription.domainEvents().isEmpty());
    }

    @Test
    public void givenTrialingSubscription_whenExecuteIncompleteCommand_shouldTransitToIncompleteState() {
        // given
        final var expectedId = new SubscriptionId("qualquerId");
        final var expectedVersion = 0;
        final var expectedAccountId = new AccountId("qualquerAccountId");
        final var expectedPlanId = new PlanId(123456L);
        final var expectedStatus = SubscriptionStatus.INCOMPLETE;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDuedate = LocalDate.now();
        final Instant expectedLastRenewDate = null;
        final var expectedLasttransactionId = UUID.randomUUID().toString();
        final var expectedEvents = 1;

        final var expectedReason = "fail to charge credit card";

        final var actualSubscription = Subscription.with(
                expectedId,
                expectedVersion,
                expectedAccountId,
                expectedPlanId,
                expectedDuedate,
                SubscriptionStatus.TRAILING,
                expectedLastRenewDate,
                null,
                expectedCreatedAt,
                expectedUpdatedAt
        );

        // when
        actualSubscription.execute(new IncompleteSubscription(expectedReason, expectedLasttransactionId));

        // then
        Assertions.assertNotNull(actualSubscription);
        Assertions.assertEquals(expectedId, actualSubscription.id());
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedPlanId, actualSubscription.planId());
        Assertions.assertEquals(expectedDuedate, actualSubscription.dueDate());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertEquals(expectedLastRenewDate, actualSubscription.lastRenewDate());
        Assertions.assertEquals(expectedLasttransactionId, actualSubscription.lastTransactionId());
        Assertions.assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        Assertions.assertTrue(actualSubscription.updatedAt().isAfter(expectedUpdatedAt));

        Assertions.assertEquals(expectedEvents, actualSubscription.domainEvents().size());
        Assertions.assertInstanceOf(SubscriptionIncomplete.class, actualSubscription.domainEvents().getFirst());
    }

    @Test
    public void givenTrialingSubscription_whenExecuteRenewCommand_shouldTransitToActiveState() {
        // given
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedId = new SubscriptionId("qualquerId");
        final var expectedVersion = 0;
        final var expectedAccountId = new AccountId("qualquerAccountId");
        final var expectedPlanId = expectedPlan.id();
        final var expectedStatus = SubscriptionStatus.ACTIVE;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDuedate = LocalDate.now().plusMonths(1);
        final var expectedLasttransactionId = UUID.randomUUID().toString();
        final var expectedEvents = 1;

        final var actualSubscription = Subscription.with(
                expectedId,
                expectedVersion,
                expectedAccountId,
                expectedPlanId,
                LocalDate.now(),
                SubscriptionStatus.TRAILING,
                null,
                null,
                expectedCreatedAt,
                expectedUpdatedAt
        );

        // when
        actualSubscription.execute(new RenewSubscription(expectedPlan, expectedLasttransactionId));

        // then
        Assertions.assertNotNull(actualSubscription);
        Assertions.assertEquals(expectedId, actualSubscription.id());
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedPlanId, actualSubscription.planId());
        Assertions.assertEquals(expectedDuedate, actualSubscription.dueDate());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertNotNull(actualSubscription.lastRenewDate());
        Assertions.assertEquals(expectedLasttransactionId, actualSubscription.lastTransactionId());
        Assertions.assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        Assertions.assertTrue(actualSubscription.updatedAt().isAfter(expectedUpdatedAt));

        Assertions.assertEquals(expectedEvents, actualSubscription.domainEvents().size());
        Assertions.assertInstanceOf(SubscriptionRenewed.class, actualSubscription.domainEvents().getFirst());
    }

    @Test
    public void givenTrialingSubscription_whenExecuteCancelCommand_shouldTransitToCanceledState() {
        // given
        final var expectedId = new SubscriptionId("qualquerId");
        final var expectedVersion = 0;
        final var expectedAccountId = new AccountId("qualquerAccountId");
        final var expectedPlanId = new PlanId(123L);
        final var expectedStatus = SubscriptionStatus.CANCELED;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDuedate = LocalDate.now().plusMonths(1);
        final var expectedLastRenewDate = InstantUtils.now();
        final var expectedLasttransactionId = UUID.randomUUID().toString();
        final var expectedEvents = 1;

        final var actualSubscription = Subscription.with(
                expectedId,
                expectedVersion,
                expectedAccountId,
                expectedPlanId,
                expectedDuedate,
                SubscriptionStatus.TRAILING,
                expectedLastRenewDate,
                expectedLasttransactionId,
                expectedCreatedAt,
                expectedUpdatedAt
        );

        // when
        actualSubscription.execute(new CancelSubscription());

        // then
        Assertions.assertNotNull(actualSubscription);
        Assertions.assertEquals(expectedId, actualSubscription.id());
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedPlanId, actualSubscription.planId());
        Assertions.assertEquals(expectedDuedate, actualSubscription.dueDate());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertNotNull(actualSubscription.lastRenewDate());
        Assertions.assertEquals(expectedLasttransactionId, actualSubscription.lastTransactionId());
        Assertions.assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        Assertions.assertTrue(actualSubscription.updatedAt().isAfter(expectedUpdatedAt));

        Assertions.assertEquals(expectedEvents, actualSubscription.domainEvents().size());
        Assertions.assertInstanceOf(SubscriptionCanceled.class, actualSubscription.domainEvents().getFirst());
    }

}
