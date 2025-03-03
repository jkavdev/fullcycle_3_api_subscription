package br.com.jkavdev.fullcycle.subscription.application.account.impl;

import br.com.jkavdev.fullcycle.subscription.application.account.RemoveFromGroup;
import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.UnitTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.GroupId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.plan.Plan;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.status.CanceledSubscriptionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

class DefaultRemoveFromGroupTest extends UnitTest {

    @InjectMocks
    DefaultRemoveFromGroup target;

    @Mock
    private IdentityProviderGateway identityProviderGateway;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @Mock
    private AccountGateway accountGateway;

    @Test
    public void givenNullInput_whenCallsExecute_shouldReturnError() {
        // given
        final RemoveFromGroupTestInput expectedInput = null;

        final var expectedErrorMessage = "input to DefaultRemoveFromGroup cannot be null";

        // when
        final var actualException = Assertions.assertThrows(IllegalArgumentException.class, () -> target.execute(expectedInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNotFoundSubscription_whenCallsExecute_shouldReturnError() {
        // given
        final var expectedInput = new RemoveFromGroupTestInput(
                "acc-123",
                "gro-123",
                "sub-123"
        );

        final var expectedErrorMessage =
                "br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription with id sub-123 was not found";

        Mockito.when(subscriptionGateway.subscriptionOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () -> target.execute(expectedInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenSubscriptionAnotherAccount_whenCallsExecute_shouldReturnError() {
        // given
        final var expectedSubscription = Fixture.Subscriptions.jhous();
        final var expectedInput = new RemoveFromGroupTestInput(
                "acc-123",
                "gro-123",
                "sub-123"
        );

        final var expectedErrorMessage =
                "br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription with id sub-123 was not found";

        Mockito.when(subscriptionGateway.subscriptionOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedSubscription));

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () -> target.execute(expectedInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenCanceledSubscriptionAndDueDatePast_whenCallsExecute_shouldCallIdentityProvider() {
        // given
        final var jhou = Fixture.Accounts.jhou();
        final var plus = Fixture.Plans.plus();
        final var expectedGroupId = new GroupId("group-123");
        final var expectedAccountId = jhou.id();
        final var jhouSubscription = newSubscriptionWith(
                expectedAccountId,
                plus,
                CanceledSubscriptionStatus.CANCELED,
                LocalDateTime.now().minusDays(1)
        );
        final var expectedSubscriptionId = jhouSubscription.id();

        Assertions.assertTrue(jhouSubscription.isCanceled(),
                "para esse teste a subscription precisa estar como cancelada");

        Mockito.when(subscriptionGateway.subscriptionOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(jhouSubscription));
        Mockito.when(accountGateway.accountOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(jhou));
        Mockito.doNothing()
                .when(identityProviderGateway)
                .removeUserFromGroup(ArgumentMatchers.any(), ArgumentMatchers.any());

        // when
        target.execute(new RemoveFromGroupTestInput(
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
                .removeUserFromGroup(jhou.userId(), expectedGroupId);
    }

    @Test
    public void givenIncompleteSubscriptionAndDueDatePast_whenCallsExecute_shouldCallIdentityProvider() {
        // given
        final var jhou = Fixture.Accounts.jhou();
        final var plus = Fixture.Plans.plus();
        final var expectedGroupId = new GroupId("group-123");
        final var expectedAccountId = jhou.id();
        final var jhouSubscription = newSubscriptionWith(
                expectedAccountId,
                plus,
                CanceledSubscriptionStatus.INCOMPLETE,
                LocalDateTime.now().minusDays(1)
        );
        final var expectedSubscriptionId = jhouSubscription.id();

        Assertions.assertTrue(jhouSubscription.isIncomplete(),
                "para esse teste a subscription precisa estar como incompleta");

        Mockito.when(subscriptionGateway.subscriptionOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(jhouSubscription));
        Mockito.when(accountGateway.accountOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(jhou));
        Mockito.doNothing()
                .when(identityProviderGateway)
                .removeUserFromGroup(ArgumentMatchers.any(), ArgumentMatchers.any());

        // when
        target.execute(new RemoveFromGroupTestInput(
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
                .removeUserFromGroup(jhou.userId(), expectedGroupId);
    }

    @Test
    public void givenOtherStatusSubscriptionAndDueDatePast_whenCallsExecute_shouldNotCallIdentityProvider() {
        // given
        final var jhou = Fixture.Accounts.jhou();
        final var plus = Fixture.Plans.plus();
        final var expectedGroupId = new GroupId("group-123");
        final var expectedAccountId = jhou.id();
        final var jhouSubscription = newSubscriptionWith(
                expectedAccountId,
                plus,
                CanceledSubscriptionStatus.TRAILING,
                LocalDateTime.now().minusDays(1)
        );
        final var expectedSubscriptionId = jhouSubscription.id();

        Assertions.assertTrue(jhouSubscription.isTrail(),
                "para esse teste a subscription precisa estar como em teste");

        Mockito.when(subscriptionGateway.subscriptionOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(jhouSubscription));

        // when
        target.execute(new RemoveFromGroupTestInput(
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
                .removeUserFromGroup(jhou.userId(), expectedGroupId);
    }

    @Test
    public void givenSubscriptionCurrentDueDate_whenCallsExecute_shouldNotCallIdentityProvider() {
        // given
        final var jhou = Fixture.Accounts.jhou();
        final var plus = Fixture.Plans.plus();
        final var expectedGroupId = new GroupId("group-123");
        final var expectedAccountId = jhou.id();
        final var jhouSubscription = newSubscriptionWith(
                expectedAccountId,
                plus,
                CanceledSubscriptionStatus.CANCELED,
                LocalDateTime.now()
        );
        final var expectedSubscriptionId = jhouSubscription.id();

        Assertions.assertTrue(jhouSubscription.isCanceled(),
                "para esse teste a subscription precisa estar como em teste");

        Mockito.when(subscriptionGateway.subscriptionOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(jhouSubscription));

        // when
        target.execute(new RemoveFromGroupTestInput(
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
                .removeUserFromGroup(jhou.userId(), expectedGroupId);
    }

    private static Subscription newSubscriptionWith(
            final AccountId accountId,
            final Plan plan,
            final String status,
            final LocalDateTime date
    ) {
        final var instant = date.toInstant(ZoneOffset.UTC);
        return Subscription.with(
                new SubscriptionId("sub123"),
                1,
                accountId,
                plan.id(),
                date.toLocalDate(),
                status,
                instant,
                "a123",
                instant,
                instant
        );
    }

    record RemoveFromGroupTestInput(
            String accountId,
            String groupId,
            String subscriptionId
    ) implements RemoveFromGroup.Input {

    }

}