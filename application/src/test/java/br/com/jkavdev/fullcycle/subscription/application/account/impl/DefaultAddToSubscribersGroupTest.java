package br.com.jkavdev.fullcycle.subscription.application.account.impl;

import br.com.jkavdev.fullcycle.subscription.application.account.AddToSubscribersGroup;
import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.UnitTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.GroupId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

class DefaultAddToSubscribersGroupTest extends UnitTest {

    @InjectMocks
    DefaultAddToSubscribersGroup target;

    @Mock
    private IdentityProviderGateway identityProviderGateway;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @Mock
    private AccountGateway accountGateway;

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
        target.execute(new AddToSubscribersGroupTestInput(
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

    record AddToSubscribersGroupTestInput(
            String accountId,
            String groupId,
            String subscriptionId
    ) implements AddToSubscribersGroup.Input {

    }

}