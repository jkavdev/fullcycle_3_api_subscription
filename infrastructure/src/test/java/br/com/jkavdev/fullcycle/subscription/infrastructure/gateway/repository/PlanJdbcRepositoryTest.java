package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.AbstractRepositoryTest;
import br.com.jkavdev.fullcycle.subscription.domain.money.Money;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;

class PlanJdbcRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private JdbcClient jdbcClient;

    @Test
    void testAssertDependencies() {
        Assertions.assertNotNull(planRepository());
    }

    @Test
    @Sql({"classpath:/sql/plans/seed-plan-master.sql"})
    public void givenPersistedPlan_whenQueriesSuccessfully_shouldReturnIt() {
        // given
        Assertions.assertEquals(1, countPlans());

        final var expectedName = "Master";
        final var expectedVersion = 1;
        final var expectedDescription = "O plano mais custo benefício";
        final var expectedActive = false;
        final var expectedPrice = new Money("BRL", 20.0);
        final var expectedPlanId = new PlanId(1L);
        final var expectedCreatedAt = Instant.parse("2024-04-28T10:57:11.111Z");
        final var expectedUpdatedAt = Instant.parse("2024-04-28T10:58:11.111Z");
        final var expectedDeletedAt = Instant.parse("2024-04-28T10:59:11.111Z");

        // when
        final var actualAccount = planRepository().planOfId(expectedPlanId).orElseThrow();

        // then
        Assertions.assertEquals(expectedPlanId, actualAccount.id());
        Assertions.assertEquals(expectedVersion, actualAccount.version());
        Assertions.assertEquals(expectedName, actualAccount.name());
        Assertions.assertEquals(expectedDescription, actualAccount.description());
        Assertions.assertEquals(expectedActive, actualAccount.active());
        Assertions.assertEquals(expectedPrice, actualAccount.price());
        Assertions.assertEquals(expectedCreatedAt, actualAccount.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualAccount.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, actualAccount.deletedAt());
    }

}