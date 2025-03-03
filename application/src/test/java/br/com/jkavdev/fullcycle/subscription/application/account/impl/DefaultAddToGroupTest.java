package br.com.jkavdev.fullcycle.subscription.application.account.impl;

import br.com.jkavdev.fullcycle.subscription.application.account.AddToGroup;
import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.UnitTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.GroupId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionCommand;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

class DefaultAddToGroupTest extends UnitTest {

    @InjectMocks
    DefaultAddToGroup target;

    @Mock
    private IdentityProviderGateway identityProviderGateway;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @Mock
    private AccountGateway accountGateway;

    @Test
    public void givenNullInput_whenCallsExecute_shouldReturnError() {
        // given
        final AddToGroupTestInput expectedInput = null;

        final var expectedErrorMessage = "input to DefaultAddToGroup cannot be null";

        // when
        final var actualException = Assertions.assertThrows(IllegalArgumentException.class, () -> target.execute(expectedInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNotFoundSubscription_whenCallsExecute_shouldReturnNotFoundError() {
        // given
        final var jhou = Fixture.Accounts.jhou();
        final var expectedGroupId = new GroupId("group-123");
        final var expectedSubscriptionId = new SubscriptionId("sub-123");

        final var expectedErrorMessage =
                "br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription with id sub-123 was not found";

        Mockito.when(subscriptionGateway.subscriptionOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                target.execute(new AddToGroupTestInput(
                        jhou.id().value(),
                        expectedGroupId.value(),
                        expectedSubscriptionId.value()
                )));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenSubscriptionFromAnotherAccount_whenCallsExecute_shouldReturnNotFoundError() {
        // given
        final var jhouSub = Subscription.newSubscription(
                new SubscriptionId("outra-123"),
                new AccountId("outra-123"),
                Fixture.Plans.plus()
        );

        final var jhou = Fixture.Accounts.jhou();
        final var expectedGroupId = new GroupId("group-123");
        final var expectedSubscriptionId = jhouSub.id();

        final var expectedErrorMessage =
                "br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription with id outra-123 was not found";

        Mockito.when(subscriptionGateway.subscriptionOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(jhouSub));

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                target.execute(new AddToGroupTestInput(
                        jhou.id().value(),
                        expectedGroupId.value(),
                        expectedSubscriptionId.value()
                )));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenTrailSubscription_whenCallsExecute_shouldCallIdentityProvider() {
        // given
        final var jhou = Fixture.Accounts.jhou();
        final var jhouSubscription = Fixture.Subscriptions.jhous();
        final var expectedGroupId = new GroupId("group-123");
        final var expectedAccountId = jhou.id();
        final var expectedSubscriptionId = new SubscriptionId("sub-123");

        Assertions.assertTrue(jhouSubscription.isTrail(),
                "para esse teste a subscription precisa estar como trail");

        Mockito.when(subscriptionGateway.subscriptionOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(jhouSubscription));
        Mockito.when(accountGateway.accountOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(jhou));
        Mockito.doNothing()
                .when(identityProviderGateway)
                .addUserToGroup(ArgumentMatchers.any(), ArgumentMatchers.any());

        // when
        target.execute(new AddToGroupTestInput(
                expectedAccountId.value(),
                expectedGroupId.value(),
                expectedSubscriptionId.value()
        ));

        // then
        Mockito.verify(subscriptionGateway, Mockito.times(1))
                .subscriptionOfId(expectedSubscriptionId);

        Mockito.verify(accountGateway, Mockito.times(1))
                .accountOfId(jhou.id());

        Mockito.verify(identityProviderGateway, Mockito.times(1))
                .addUserToGroup(jhou.userId(), expectedGroupId);
    }

    @Test
    public void givenActiveSubscription_whenCallsExecute_shouldCallIdentityProvider() {
        // given
        final var jhou = Fixture.Accounts.jhou();
        final var jhouSubscription = Fixture.Subscriptions.jhous();
        jhouSubscription.status().active();

        final var expectedGroupId = new GroupId("group-123");
        final var expectedAccountId = jhou.id();
        final var expectedSubscriptionId = new SubscriptionId("sub-123");

        Assertions.assertTrue(jhouSubscription.isActive(),
                "para esse teste a subscription precisa estar como active");

        Mockito.when(subscriptionGateway.subscriptionOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(jhouSubscription));
        Mockito.when(accountGateway.accountOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(jhou));
        Mockito.doNothing()
                .when(identityProviderGateway)
                .addUserToGroup(ArgumentMatchers.any(), ArgumentMatchers.any());

        // when
        target.execute(new AddToGroupTestInput(
                expectedAccountId.value(),
                expectedGroupId.value(),
                expectedSubscriptionId.value()
        ));

        // then
        Mockito.verify(subscriptionGateway, Mockito.times(1))
                .subscriptionOfId(expectedSubscriptionId);

        Mockito.verify(accountGateway, Mockito.times(1))
                .accountOfId(jhou.id());

        Mockito.verify(identityProviderGateway, Mockito.times(1))
                .addUserToGroup(jhou.userId(), expectedGroupId);
    }

    @Test
    public void givenOtherSubscriptionStatus_whenCallsExecute_shouldNotCallIdentityProvider() {
        // given
        final var jhou = Fixture.Accounts.jhou();
        final var jhouSubscription = Fixture.Subscriptions.jhous();
        jhouSubscription.execute(new SubscriptionCommand.IncompleteSubscription(
                "falha ao renovar subscricao",
                "tra-123"
        ));
        final var expectedGroupId = new GroupId("group-123");
        final var expectedAccountId = jhou.id();
        final var expectedSubscriptionId = new SubscriptionId("sub-123");

        Assertions.assertTrue(jhouSubscription.isIncomplete(),
                "para esse teste a subscription precisa estar como incomplete");

        Mockito.when(subscriptionGateway.subscriptionOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(jhouSubscription));

        // when
        target.execute(new AddToGroupTestInput(
                expectedAccountId.value(),
                expectedGroupId.value(),
                expectedSubscriptionId.value()
        ));

        // then
        Mockito.verify(subscriptionGateway, Mockito.times(1))
                .subscriptionOfId(expectedSubscriptionId);

        Mockito.verify(accountGateway, Mockito.never())
                .accountOfId(jhou.id());

        Mockito.verify(identityProviderGateway, Mockito.never())
                .addUserToGroup(jhou.userId(), expectedGroupId);
    }

    record AddToGroupTestInput(
            String accountId,
            String groupId,
            String subscriptionId
    ) implements AddToGroup.Input {

    }

}