package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.AbstractRepositoryTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
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

}