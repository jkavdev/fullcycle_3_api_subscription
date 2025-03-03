package br.com.jkavdev.fullcycle.subscription.application.subscription.impl;

import br.com.jkavdev.fullcycle.subscription.application.subscription.CreateSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.UnitTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanCommand;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionCommand;
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
    public void givenNullInput_whenCallsCreateSubscription_shouldReturnError() {
        // given
        final CreateSubscriptionTestInput expectedInput = null;

        final var expectedErrorMessage = "input to DefaultCreateSubscription cannot be null";

        // when
        final var actualException = Assertions.assertThrows(IllegalArgumentException.class, () -> target.execute(expectedInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAccountWithActiveSubscription_whenCallsCreateSubscription_shouldReturnActiveSubscriptionError() {
        // given
        final var expectedAccount = Fixture.Accounts.jhou();
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedSubscription = Fixture.Subscriptions.jhous();

        Mockito.when(subscriptionGateway.latestSubscriptionOfAccount(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedSubscription));

        final var expectedErrorMessage =
                "account %s already has a active subscription".formatted(expectedAccount.id().value());

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> target.execute(
                        new CreateSubscriptionTestInput(expectedAccount.id().value(), expectedPlan.id().value())
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidPlan_whenCallsCreateSubscription_shouldReturnNotFoundError() {
        // given
        final var expectedAccount = Fixture.Accounts.jhou();
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedLastestSubscription = Optional.<Subscription>empty();

        Mockito.when(subscriptionGateway.latestSubscriptionOfAccount(ArgumentMatchers.any()))
                .thenReturn(expectedLastestSubscription);
        Mockito.when(planGateway.planOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

        final var expectedErrorMessage =
                "br.com.jkavdev.fullcycle.subscription.domain.plan.Plan with id 123456 was not found";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> target.execute(
                        new CreateSubscriptionTestInput(expectedAccount.id().value(), expectedPlan.id().value())
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInactivePlan_whenCallsCreateSubscription_shouldReturnNotFoundError() {
        // given
        final var expectedAccount = Fixture.Accounts.jhou();
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedLastestSubscription = Optional.<Subscription>empty();
        expectedPlan.execute(new PlanCommand.InactivePlan());

        Mockito.when(subscriptionGateway.latestSubscriptionOfAccount(ArgumentMatchers.any()))
                .thenReturn(expectedLastestSubscription);
        Mockito.when(planGateway.planOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedPlan));

        final var expectedErrorMessage =
                "br.com.jkavdev.fullcycle.subscription.domain.plan.Plan with id 123456 was not found";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> target.execute(
                        new CreateSubscriptionTestInput(expectedAccount.id().value(), expectedPlan.id().value())
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidAccount_whenCallsCreateSubscription_shouldReturnNotFoundError() {
        // given
        final var expectedAccount = Fixture.Accounts.jhou();
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedLastestSubscription = Optional.<Subscription>empty();

        Mockito.when(subscriptionGateway.latestSubscriptionOfAccount(ArgumentMatchers.any()))
                .thenReturn(expectedLastestSubscription);
        Mockito.when(planGateway.planOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedPlan));
        Mockito.when(accountGateway.accountOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

        final var expectedErrorMessage =
                "br.com.jkavdev.fullcycle.subscription.domain.account.Account with id acc-jhou was not found";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> target.execute(
                        new CreateSubscriptionTestInput(expectedAccount.id().value(), expectedPlan.id().value())
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAccountWithCanceledSubscription_whenCallsCreateSubscription_shouldReturnNewSubscription() {
        // given
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedAccount = Fixture.Accounts.jhou();
        final var expectedSubscriptionId = new SubscriptionId("jhousub123");

        final var jhousSub = Fixture.Subscriptions.jhous();
        jhousSub.execute(new SubscriptionCommand.CancelSubscription());

        Mockito.when(subscriptionGateway.latestSubscriptionOfAccount(ArgumentMatchers.any()))
                .thenReturn(Optional.of(jhousSub));
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