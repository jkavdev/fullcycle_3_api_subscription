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
class CanceledSubscriptionStatusTest {

    @Test
    public void givenInstance_whenCallsToString_shouldReturnValue() {
        // given
        final var expectedString = "canceled";
        final var one = new CanceledSubscriptionStatus(Subscription.newSubscription(
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
        final var one = new CanceledSubscriptionStatus(Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        ));
        final var two = new CanceledSubscriptionStatus(Subscription.newSubscription(
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
        final var one = new CanceledSubscriptionStatus(Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        ));
        final var two = new CanceledSubscriptionStatus(Subscription.newSubscription(
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
    public void givenCanceledStatus_whenCallsTrailing_shouldReturnError() {
        // given
        final var expectedStatusClass = CanceledSubscriptionStatus.class;
        final var expectedSubscription = canceledSubscription();
        final var target = new CanceledSubscriptionStatus(expectedSubscription);
        final var expectedErrorMessage = "subscription with status canceled can't transit to trailing";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, target::trailing);

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenCanceledStatus_whenCallsCancel_shouldDoNothing() {
        // given
        final var expectedStatusClass = CanceledSubscriptionStatus.class;
        final var expectedSubscription = canceledSubscription();
        final var target = new CanceledSubscriptionStatus(expectedSubscription);

        // when
        target.cancel();

        // then
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenCanceledStatus_whenCallsActive_shouldReturnError() {
        // given
        final var expectedStatusClass = CanceledSubscriptionStatus.class;
        final var expectedSubscription = canceledSubscription();
        final var target = new CanceledSubscriptionStatus(expectedSubscription);
        final var expectedErrorMessage = "subscription with status canceled can't transit to active";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, target::active);

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenCanceledStatus_whenCallsIncomplete_shouldReturnError() {
        // given
        final var expectedStatusClass = CanceledSubscriptionStatus.class;
        final var expectedSubscription = canceledSubscription();
        final var target = new CanceledSubscriptionStatus(expectedSubscription);
        final var expectedErrorMessage = "subscription with status canceled can't transit to incomplete";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, target::incomplete);

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Assertions.assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    private static Subscription canceledSubscription() {
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        );
        expectedSubscription.status().cancel();
        return expectedSubscription;
    }

}