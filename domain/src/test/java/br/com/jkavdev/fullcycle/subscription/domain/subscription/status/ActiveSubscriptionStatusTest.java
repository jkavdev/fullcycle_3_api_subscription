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
class ActiveSubscriptionStatusTest {

    @Test
    public void givenInstance_whenCallsToString_shouldReturnValue() {
        // given
        final var expectedString = "active";
        final var one = new ActiveSubscriptionStatus(Subscription.newSubscription(
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
        final var one = new ActiveSubscriptionStatus(Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        ));
        final var two = new ActiveSubscriptionStatus(Subscription.newSubscription(
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
        final var one = new ActiveSubscriptionStatus(Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        ));
        final var two = new ActiveSubscriptionStatus(Subscription.newSubscription(
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
    public void givenActiveStatus_whenCallsTrailing_shouldReturnError() {
        // given
        final var expectedStatusClass = ActiveSubscriptionStatus.class;
        final var expectedSubscription = activeSubscription();
        final var target = new ActiveSubscriptionStatus(expectedSubscription);
        final var expectedErrorMessage = "subscription with status active can't transit to trailing";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, target::trailing);

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenActiveStatus_whenCallsCancel_shouldTransitToCanceledStatus() {
        // given
        final var expectedStatusClass = CanceledSubscriptionStatus.class;
        final var expectedSubscription = activeSubscription();
        final var target = new ActiveSubscriptionStatus(expectedSubscription);

        // when
        target.cancel();

        // then
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenActiveStatus_whenCallsActive_shouldDoNothing() {
        // given
        final var expectedStatusClass = ActiveSubscriptionStatus.class;
        final var expectedSubscription = activeSubscription();
        final var target = new ActiveSubscriptionStatus(expectedSubscription);

        // when
        target.active();

        // then
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenActiveStatus_whenCallsIncomplete_shouldTransitToIncompleteStatus() {
        // given
        final var expectedStatusClass = IncompleteSubscriptionStatus.class;
        final var expectedSubscription = activeSubscription();
        final var target = new ActiveSubscriptionStatus(expectedSubscription);

        // when
        target.incomplete();

        // then
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    private static Subscription activeSubscription() {
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        );
        expectedSubscription.status().active();
        return expectedSubscription;
    }

}