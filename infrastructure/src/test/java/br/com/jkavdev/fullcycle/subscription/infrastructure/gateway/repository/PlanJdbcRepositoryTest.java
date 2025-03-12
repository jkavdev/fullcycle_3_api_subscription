package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.AbstractRepositoryTest;
import br.com.jkavdev.fullcycle.subscription.domain.money.Money;
import br.com.jkavdev.fullcycle.subscription.domain.plan.Plan;
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
        final var actualPlan = planRepository().planOfId(expectedPlanId).orElseThrow();

        // then
        Assertions.assertEquals(expectedPlanId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertEquals(expectedCreatedAt, actualPlan.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualPlan.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, actualPlan.deletedAt());
    }

    @Test
    public void givenEmptyTable_whenInsertSuccessfully_shouldBePersisted() {
        // given
        Assertions.assertEquals(0, countPlans());

        final var expectedName = "Master";
        final var expectedVersion = 1;
        final var expectedDescription = "O plano mais custo benefício";
        final var expectedActive = false;
        final var expectedPrice = new Money("BRL", 20.0);
        final var expectedPlanId = new PlanId(1L);
        final var expectedCreatedAt = Instant.parse("2024-04-28T10:57:11.111Z");
        final var expectedUpdatedAt = Instant.parse("2024-04-28T10:58:11.111Z");
        final var expectedDeletedAt = Instant.parse("2024-04-28T10:59:11.111Z");

        final var plan = Plan.with(
                expectedPlanId,
                0,
                expectedName,
                expectedDescription,
                expectedActive,
                expectedPrice,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt
        );

        Assertions.assertEquals(expectedPlanId, plan.id());
        Assertions.assertEquals(0, plan.version());
        Assertions.assertEquals(expectedName, plan.name());
        Assertions.assertEquals(expectedDescription, plan.description());
        Assertions.assertEquals(expectedActive, plan.active());
        Assertions.assertEquals(expectedPrice, plan.price());
        Assertions.assertEquals(expectedCreatedAt, plan.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, plan.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, plan.deletedAt());

        // when
        final var responsePlan = planRepository().save(plan);
        Assertions.assertEquals(expectedPlanId, responsePlan.id());
        Assertions.assertEquals(0, responsePlan.version());
        Assertions.assertEquals(expectedName, responsePlan.name());
        Assertions.assertEquals(expectedDescription, responsePlan.description());
        Assertions.assertEquals(expectedActive, responsePlan.active());
        Assertions.assertEquals(expectedPrice, responsePlan.price());
        Assertions.assertEquals(expectedCreatedAt, responsePlan.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, responsePlan.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, responsePlan.deletedAt());

        // then
        final var actualPlan = planRepository().planOfId(expectedPlanId).orElseThrow();
        Assertions.assertEquals(expectedPlanId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertEquals(expectedCreatedAt, actualPlan.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualPlan.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, actualPlan.deletedAt());
    }

    @Test
    @Sql({"classpath:/sql/plans/seed-plan-master.sql"})
    public void givenMasterPlan_whenUpdateSuccessfully_shouldBePersisted() {
        // given
        Assertions.assertEquals(1, countPlans());
        final var expectedPlanId = new PlanId(1L);

        final var persistedPlan = planRepository().planOfId(expectedPlanId).orElseThrow();
        Assertions.assertEquals(expectedPlanId, persistedPlan.id());
        Assertions.assertEquals(1, persistedPlan.version());
        Assertions.assertEquals("Master", persistedPlan.name());
        Assertions.assertEquals("O plano mais custo benefício", persistedPlan.description());
        Assertions.assertEquals(false, persistedPlan.active());
        Assertions.assertEquals(new Money("BRL", 20.0), persistedPlan.price());
        Assertions.assertEquals(Instant.parse("2024-04-28T10:57:11.111Z"), persistedPlan.createdAt());
        Assertions.assertEquals(Instant.parse("2024-04-28T10:58:11.111Z"), persistedPlan.updatedAt());
        Assertions.assertEquals(Instant.parse("2024-04-28T10:59:11.111Z"), persistedPlan.deletedAt());

        final var expectedName = "Plus";
        final var expectedVersion = 2;
        final var expectedDescription = "O plano PLUS";
        final var expectedActive = true;
        final var expectedPrice = new Money("USD", 30.0);
        final var expectedCreatedAt = Instant.parse("2024-04-29T10:57:11.111Z");
        final var expectedUpdatedAt = Instant.parse("2024-04-29T10:58:11.111Z");
        final var expectedDeletedAt = Instant.parse("2024-04-29T10:59:11.111Z");

        final var plan = Plan.with(
                expectedPlanId,
                1,
                expectedName,
                expectedDescription,
                expectedActive,
                expectedPrice,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt
        );

        Assertions.assertEquals(expectedPlanId, plan.id());
        Assertions.assertEquals(1, plan.version());
        Assertions.assertEquals(expectedName, plan.name());
        Assertions.assertEquals(expectedDescription, plan.description());
        Assertions.assertEquals(expectedActive, plan.active());
        Assertions.assertEquals(expectedPrice, plan.price());
        Assertions.assertEquals(expectedCreatedAt, plan.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, plan.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, plan.deletedAt());

        // when
        final var responsePlan = planRepository().save(plan);
        Assertions.assertEquals(expectedPlanId, responsePlan.id());
        Assertions.assertEquals(1, responsePlan.version());
        Assertions.assertEquals(expectedName, responsePlan.name());
        Assertions.assertEquals(expectedDescription, responsePlan.description());
        Assertions.assertEquals(expectedActive, responsePlan.active());
        Assertions.assertEquals(expectedPrice, responsePlan.price());
        Assertions.assertEquals(expectedCreatedAt, responsePlan.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, responsePlan.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, responsePlan.deletedAt());

        // then
        final var actualPlan = planRepository().planOfId(expectedPlanId).orElseThrow();
        Assertions.assertEquals(expectedPlanId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertEquals(expectedCreatedAt, actualPlan.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualPlan.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, actualPlan.deletedAt());
    }

}