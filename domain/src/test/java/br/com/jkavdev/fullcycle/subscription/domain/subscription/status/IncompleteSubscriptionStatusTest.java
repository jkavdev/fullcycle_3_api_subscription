package br.com.jkavdev.fullcycle.subscription.domain.subscription.status;

import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 1 - transicao para active
 * 2 - transicao para canceled
 * 3 - transicao para trailing
 * 4 - transicao para incomplete
 */
class IncompleteSubscriptionStatusTest {

    @Test
    public void givenInstance_whenCallsToString_shouldReturnValue() {
        // given
        final var expectedString = "incomplete";
        final var one = new IncompleteSubscriptionStatus(Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        ));

        // when
        final var actualToString = one.toString();

        // then
        Assertions.assertEquals(expectedString, actualToString);
    }

    @Test
    public void givenTwoInstances_whenCallsEquals_shouldBeEquals() {
        // given
        final var expectedEquals = true;
        final var one = new IncompleteSubscriptionStatus(Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        ));
        final var two = new IncompleteSubscriptionStatus(Subscription.newSubscription(
                new SubscriptionId("qualquerId2"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        ));

        // when
        final var actualEquals = one.equals(two);

        // then
        Assertions.assertEquals(expectedEquals, actualEquals,
                "o equals deveria levar em conta apenas a classe do status e nao a subscription");
    }

    @Test
    public void givenTwoInstances_whenCallsHash_shouldBeEquals() {
        // given
        final var one = new IncompleteSubscriptionStatus(Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        ));
        final var two = new IncompleteSubscriptionStatus(Subscription.newSubscription(
                new SubscriptionId("qualquerId2"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        ));

        // when
        // then
        Assertions.assertEquals(one.hashCode(), two.hashCode(),
                "o hash deveria levar em conta apenas a classe do status e nao a subscription");
    }

    @Test
    public void givenIncompleteStatus_whenCallsTrailing_shouldReturnError() {
        // given
        final var expectedStatusClass = IncompleteSubscriptionStatus.class;
        final var expectedSubscription = incompleteSubscription();
        final var target = new IncompleteSubscriptionStatus(expectedSubscription);
        final var expectedErrorMessage = "subscription with status incomplete can't transit to trailing";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, target::trailing);

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenIncompleteStatus_whenCallsCancel_shouldTransitToCanceledStatus() {
        // given
        final var expectedStatusClass = CanceledSubscriptionStatus.class;
        final var expectedSubscription = incompleteSubscription();
        final var target = new IncompleteSubscriptionStatus(expectedSubscription);

        // when
        target.cancel();

        // then
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenIncompleteStatus_whenCallsActive_shouldTransitToActiveStatus() {
        // given
        final var expectedStatusClass = ActiveSubscriptionStatus.class;
        final var expectedSubscription = incompleteSubscription();
        final var target = new IncompleteSubscriptionStatus(expectedSubscription);

        // when
        target.active();

        // then
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenIncompleteStatus_whenCallsIncomplete_shouldDoNothing() {
        // given
        final var expectedStatusClass = IncompleteSubscriptionStatus.class;
        final var expectedSubscription = incompleteSubscription();
        final var target = new IncompleteSubscriptionStatus(expectedSubscription);

        // when
        target.incomplete();

        // then
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    private static Subscription incompleteSubscription() {
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        );
        expectedSubscription.status().incomplete();
        return expectedSubscription;
    }

}