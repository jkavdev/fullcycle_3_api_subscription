package br.com.jkavdev.fullcycle.subscription.application.subscription.impl;

import br.com.jkavdev.fullcycle.subscription.application.subscription.CreateSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.UnitTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

// TODO: implementar o restante dos testes unitarios

class DefaultCreateSubscriptionTest extends UnitTest {

    @InjectMocks
    private DefaultCreateSubscription target;

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private PlanGateway planGateway;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @Captor
    ArgumentCaptor<Subscription> captor;

    @Test
    public void givenValidAccountAndPlan_whenCallsCreateSubscription_shouldReturnNewSubscription() {
        // given
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedAccount = Fixture.Accounts.jhou();
        final var expectedSubscriptionId = new SubscriptionId("jhousub123");

        Mockito.when(subscriptionGateway.latestSubscriptionOfAccount(ArgumentMatchers.any()))
                .thenReturn(Optional.empty());
        Mockito.when(accountGateway.accountOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedAccount));
        Mockito.when(planGateway.planOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedPlan));
        Mockito.when(subscriptionGateway.nextId())
                .thenReturn(expectedSubscriptionId);
        Mockito.when(subscriptionGateway.save(ArgumentMatchers.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var actualOutput =
                target.execute(new CreateSubscriptionTestInput(expectedAccount.id().value(), expectedPlan.id().value()));

        // then
        Assertions.assertEquals(expectedSubscriptionId, actualOutput.subscriptionId());

        Mockito.verify(subscriptionGateway, Mockito.times(1))
                .save(captor.capture());

        final var actualSubscription = captor.getValue();
        Assertions.assertEquals(expectedSubscriptionId, actualSubscription.id());
        Assertions.assertEquals(expectedPlan.id(), actualSubscription.planId());
        Assertions.assertEquals(expectedAccount.id(), actualSubscription.accountId());
        Assertions.assertTrue(actualSubscription.isTrail());
        Assertions.assertNotNull(actualSubscription.dueDate());
    }

    record CreateSubscriptionTestInput(
            String accountId,
            Long planIn
    ) implements CreateSubscription.Input {
    }

}