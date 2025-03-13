package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.AbstractRepositoryTest;
import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.*;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionCommand.IncompleteSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.status.ActiveSubscriptionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.time.LocalDate;

class SubscriptionJdbcRepositoryTest extends AbstractRepositoryTest {

    @Test
    void testAssertDependencies() {
        Assertions.assertNotNull(subscriptionRepository());
    }

    @Test
    @Sql({"classpath:/sql/subscriptions/seed-subscription-jhou.sql"})
    public void givenPersistedSubscription_whenQueriesSuccessfully_shouldReturnIt() {
        // given
        Assertions.assertEquals(1, countSubscriptions());

        final var expectedId = new SubscriptionId("5783bdbcbb2347eb8883e969f14d350c");
        final var expectedAccountId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        final var expectedVersion = 1;
        final var expectedPlanId = new PlanId(1L);
        final var expectedStatus = ActiveSubscriptionStatus.ACTIVE;
        final var expectedCreatedAt = Instant.parse("2024-04-28T10:58:11.111Z");
        final var expectedUpdatedAt = Instant.parse("2024-04-28T10:59:11.111Z");
        final var expectedDueDate = LocalDate.parse("2024-05-27");
        final var expectedLastRenewDate = Instant.parse("2024-04-27T10:59:11.111Z");
        final var expectedTransactionId = "560f4e6a-79fa-473c-b7cb-a5b2bb4e6c8a";

        // when
        final var actualSubscription = subscriptionRepository().subscriptionOfId(expectedId).orElseThrow();

        // then
        Assertions.assertEquals(expectedId, actualSubscription.id());
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedPlanId, actualSubscription.planId());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualSubscription.updatedAt());
        Assertions.assertEquals(expectedDueDate, actualSubscription.dueDate());
        Assertions.assertEquals(expectedLastRenewDate, actualSubscription.lastRenewDate());
        Assertions.assertEquals(expectedTransactionId, actualSubscription.lastTransactionId());
    }

    @Test
    @Sql({"classpath:/sql/subscriptions/seed-subscription-jhou.sql"})
    public void givenPersistedSubscription_whenQueriesLatestSubscriptionOfAccountIdSuccessfully_shouldReturnIt() {
        // given
        Assertions.assertEquals(1, countSubscriptions());

        final var expectedId = new SubscriptionId("5783bdbcbb2347eb8883e969f14d350c");
        final var expectedAccountId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        final var expectedVersion = 1;
        final var expectedPlanId = new PlanId(1L);
        final var expectedStatus = ActiveSubscriptionStatus.ACTIVE;
        final var expectedCreatedAt = Instant.parse("2024-04-28T10:58:11.111Z");
        final var expectedUpdatedAt = Instant.parse("2024-04-28T10:59:11.111Z");
        final var expectedDueDate = LocalDate.parse("2024-05-27");
        final var expectedLastRenewDate = Instant.parse("2024-04-27T10:59:11.111Z");
        final var expectedTransactionId = "560f4e6a-79fa-473c-b7cb-a5b2bb4e6c8a";

        // when
        final var actualSubscription = subscriptionRepository().latestSubscriptionOfAccount(expectedAccountId)
                .orElseThrow();

        // then
        Assertions.assertEquals(expectedId, actualSubscription.id());
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedPlanId, actualSubscription.planId());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualSubscription.updatedAt());
        Assertions.assertEquals(expectedDueDate, actualSubscription.dueDate());
        Assertions.assertEquals(expectedLastRenewDate, actualSubscription.lastRenewDate());
        Assertions.assertEquals(expectedTransactionId, actualSubscription.lastTransactionId());
    }

    @Test
    public void givenEmptyTable_whenInsertsSuccessfully_shouldBePersisted() {
        // given
        Assertions.assertEquals(0, countSubscriptions());

        final var plus = Fixture.Plans.plus();

        final var expectedId = new SubscriptionId("5783bdbcbb2347eb8883e969f14d350c");
        final var expectedAccountId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        final var expectedVersion = 1;
        final var expectedPlanId = plus.id();
        final var expectedStatus = ActiveSubscriptionStatus.TRAILING;
        final var expectedDueDate = LocalDate.now().plusMonths(1);

        final var newSubscription = Subscription.newSubscription(expectedId, expectedAccountId, plus);

        // when
        subscriptionRepository().save(newSubscription);
        Assertions.assertEquals(expectedId, newSubscription.id());
        Assertions.assertEquals(0, newSubscription.version());
        Assertions.assertEquals(expectedAccountId, newSubscription.accountId());
        Assertions.assertEquals(expectedPlanId, newSubscription.planId());
        Assertions.assertEquals(expectedStatus, newSubscription.status().value());

        // then
        Assertions.assertEquals(1, countSubscriptions());

        final var actualSubscription = subscriptionRepository().subscriptionOfId(expectedId).orElseThrow();
        Assertions.assertEquals(expectedId, actualSubscription.id());
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedPlanId, actualSubscription.planId());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertNotNull(actualSubscription.createdAt());
        Assertions.assertNotNull(actualSubscription.updatedAt());
        Assertions.assertEquals(expectedDueDate, actualSubscription.dueDate());
        Assertions.assertNull(actualSubscription.lastRenewDate());
        Assertions.assertNull(actualSubscription.lastTransactionId());

        final var actualEvents = eventRepository()
                .allEventsOfAggregate(expectedId.value(), SubscriptionEvent.TYPE);
        Assertions.assertEquals(1, actualEvents.size());

        final var actualEvent = (SubscriptionCreated) actualEvents.getFirst();
        Assertions.assertEquals(expectedId.value(), actualEvent.subscriptionId());
        Assertions.assertEquals(expectedAccountId.value(), actualEvent.accountId());
        Assertions.assertEquals(expectedPlanId.value(), actualEvent.planId());
        Assertions.assertNotNull(actualEvent.occurredOn());

    }

    @Test
    @Sql({"classpath:/sql/subscriptions/seed-subscription-jhou.sql"})
    public void givenPersistedSubscription_whenUpdateSuccessfully_shouldBePersisted() {
        // given
        Assertions.assertEquals(1, countSubscriptions());

        final var expectedId = new SubscriptionId("5783bdbcbb2347eb8883e969f14d350c");
        final var expectedAccountId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        final var expectedVersion = 2;
        final var expectedPlanId = new PlanId(1L);
        final var expectedStatus = ActiveSubscriptionStatus.INCOMPLETE;
        final var expectedCreatedAt = Instant.parse("2024-04-28T10:58:11.111Z");
        final var expectedDueDate = LocalDate.parse("2024-05-27");
        final var expectedTransactionId = "560f4e6a-79fa-473c-b7cb-a5b2bb4e6wwq";

        final var persistedSubscription = subscriptionRepository().subscriptionOfId(expectedId).orElseThrow();
        Assertions.assertEquals(expectedId, persistedSubscription.id());
        Assertions.assertEquals(1, persistedSubscription.version());
        Assertions.assertEquals(expectedAccountId, persistedSubscription.accountId());
        Assertions.assertEquals(expectedPlanId, persistedSubscription.planId());
        Assertions.assertEquals(ActiveSubscriptionStatus.ACTIVE, persistedSubscription.status().value());
        Assertions.assertEquals(expectedCreatedAt, persistedSubscription.createdAt());
        Assertions.assertEquals(Instant.parse("2024-04-28T10:59:11.111Z"), persistedSubscription.updatedAt());
        Assertions.assertEquals(expectedDueDate, persistedSubscription.dueDate());
        Assertions.assertEquals(Instant.parse("2024-04-27T10:59:11.111Z"), persistedSubscription.lastRenewDate());
        Assertions.assertEquals("560f4e6a-79fa-473c-b7cb-a5b2bb4e6c8a", persistedSubscription.lastTransactionId());

        final var expectedReason = "no funds";
        persistedSubscription.execute(new IncompleteSubscription(expectedReason, expectedTransactionId));

        // when
        subscriptionRepository().save(persistedSubscription);

        // then
        final var actualSubscription = subscriptionRepository().subscriptionOfId(expectedId).orElseThrow();
        Assertions.assertEquals(expectedId, actualSubscription.id());
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedPlanId, actualSubscription.planId());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertNotNull(actualSubscription.createdAt());
        Assertions.assertNotNull(actualSubscription.updatedAt());
        Assertions.assertEquals(expectedDueDate, actualSubscription.dueDate());
        Assertions.assertNotNull(actualSubscription.lastRenewDate());
        Assertions.assertEquals(expectedTransactionId, actualSubscription.lastTransactionId());

        final var actualEvents = eventRepository()
                .allEventsOfAggregate(expectedId.value(), SubscriptionEvent.TYPE);
        Assertions.assertEquals(1, actualEvents.size());

        final var actualEvent = (SubscriptionIncomplete) actualEvents.getFirst();
        Assertions.assertEquals(expectedId.value(), actualEvent.subscriptionId());
        Assertions.assertEquals(expectedAccountId.value(), actualEvent.accountId());
        Assertions.assertEquals(expectedPlanId.value(), actualEvent.planId());
        Assertions.assertEquals(expectedReason, actualEvent.aReason());
        Assertions.assertEquals(expectedDueDate, actualEvent.dueDate());
        Assertions.assertNotNull(actualEvent.occurredOn());

    }

}