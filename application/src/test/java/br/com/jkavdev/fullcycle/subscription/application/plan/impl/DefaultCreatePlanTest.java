package br.com.jkavdev.fullcycle.subscription.application.plan.impl;

import br.com.jkavdev.fullcycle.subscription.application.plan.CreatePlan;
import br.com.jkavdev.fullcycle.subscription.domain.UnitTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import br.com.jkavdev.fullcycle.subscription.domain.money.Money;
import br.com.jkavdev.fullcycle.subscription.domain.plan.Plan;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanGateway;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class DefaultCreatePlanTest extends UnitTest {

    @InjectMocks
    DefaultCreatePlan target;

    @Mock
    private IdentityProviderGateway identityProviderGateway;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @Mock
    private PlanGateway planGateway;

    @Captor
    private ArgumentCaptor<Plan> captor;

    @Test
    public void givenNullInput_whenCallsExecute_shouldReturnError() {
        // given
        final CreatePlanTestInput expectedInput = null;

        final var expectedErrorMessage = "input to DefaultCreatePlan cannot be null";

        // when
        final var actualException = Assertions.assertThrows(IllegalArgumentException.class, () -> target.execute(expectedInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenValidInput_whenCallsExecute_shouldCreatePlan() {
        // given
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedPrice = 10.00;
        final var expectedCurrency = "BRL";
        final var expectedActive = true;
        final var expectedPlanId = new PlanId(999L);

        Mockito.when(planGateway.nextId())
                .thenReturn(expectedPlanId);
        Mockito.when(planGateway.save(ArgumentMatchers.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var actualOutput = target.execute(
                new CreatePlanTestInput(
                        expectedName,
                        expectedDescription,
                        expectedPrice,
                        expectedCurrency,
                        expectedActive
                )
        );

        // then
        Assertions.assertEquals(expectedPlanId, actualOutput.planId());

        Mockito.verify(planGateway, Mockito.times(1)).save(captor.capture());

        final var actualPlan = captor.getValue();
        Assertions.assertEquals(expectedPlanId, actualPlan.id());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(new Money(expectedCurrency, expectedPrice), actualPlan.price());
        Assertions.assertEquals(expectedActive, actualPlan.active());

    }


    record CreatePlanTestInput(
            String name,
            String description,
            Double price,
            String currency,
            Boolean active
    ) implements CreatePlan.Input {

    }

}