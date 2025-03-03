package br.com.jkavdev.fullcycle.subscription.application.plan.impl;

import br.com.jkavdev.fullcycle.subscription.application.plan.ChangePlan;
import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.UnitTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.money.Money;
import br.com.jkavdev.fullcycle.subscription.domain.plan.Plan;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

class DefaultChangePlanTest extends UnitTest {

    @InjectMocks
    DefaultChangePlan target;

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
        final ChangePlanTestInput expectedInput = null;

        final var expectedErrorMessage = "input to DefaultChangePlan cannot be null";

        // when
        final var actualException = Assertions.assertThrows(IllegalArgumentException.class, () -> target.execute(expectedInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidPlan_whenCallsExecute_shouldReturnError() {
        // given
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedPrice = 10.00;
        final var expectedCurrency = "USD";
        final var expectedActive = true;
        final var expectedPlanId = 999L;

        final var expectedErrorMessage = "plan with id 999 could not be found";

        Mockito.when(planGateway.planOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                target.execute(new ChangePlanTestInput(
                        expectedPlanId,
                        expectedName,
                        expectedDescription,
                        expectedPrice,
                        expectedCurrency,
                        expectedActive
                )));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenValidInput_whenCallsExecute_shouldChangePlan() {
        // given
        final var plan = Fixture.Plans.plus();
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedPrice = 10.00;
        final var expectedCurrency = "USD";
        final var expectedActive = true;
        final var expectedPlanId = plan.id();

        Mockito.when(planGateway.planOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(plan));
        Mockito.when(planGateway.save(ArgumentMatchers.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var actualOutput = target.execute(
                new ChangePlanTestInput(
                        expectedPlanId.value(),
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

    record ChangePlanTestInput(
            Long planId,
            String name,
            String description,
            Double price,
            String currency,
            Boolean active
    ) implements ChangePlan.Input {

    }

}