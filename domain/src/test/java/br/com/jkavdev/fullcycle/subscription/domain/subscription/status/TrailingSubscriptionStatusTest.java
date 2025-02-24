package br.com.jkavdev.fullcycle.subscription.domain.subscription.status;

import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
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
class TrailingSubscriptionStatusTest {

    @Test
    public void givenInstance_whenCallsToString_shouldReturnValue() {
        // given
        final var expectedString = "trailing";
        final var one = new TrailingSubscriptionStatus(Subscription.newSubscription(
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
        final var one = new TrailingSubscriptionStatus(Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        ));
        final var two = new TrailingSubscriptionStatus(Subscription.newSubscription(
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
        final var one = new TrailingSubscriptionStatus(Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        ));
        final var two = new TrailingSubscriptionStatus(Subscription.newSubscription(
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
    public void givenTrailingStatus_whenCallsActive_shouldTransitToActiveStatus() {
        // given
        final var expectedStatusClass = ActiveSubscriptionStatus.class;
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        );
        final var target = new TrailingSubscriptionStatus(expectedSubscription);

        // when
        target.active();

        // then
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenTrailingStatus_whenCallsCancel_shouldTransitToCanceledStatus() {
        // given
        final var expectedStatusClass = CanceledSubscriptionStatus.class;
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        );
        final var target = new TrailingSubscriptionStatus(expectedSubscription);

        // when
        target.cancel();

        // then
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenTrailingStatus_whenCallsTrailing_shouldDoNothing() {
        // given
        final var expectedStatusClass = TrailingSubscriptionStatus.class;
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        );
        final var target = new TrailingSubscriptionStatus(expectedSubscription);

        // when
        target.trailing();

        // then
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenTrailingStatus_whenCallsIncomplete_shouldTransitToIncompleteStatus() {
        // given
        final var expectedStatusClass = IncompleteSubscriptionStatus.class;
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        );
        final var target = new TrailingSubscriptionStatus(expectedSubscription);

        // when
        target.incomplete();

        // then
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

}