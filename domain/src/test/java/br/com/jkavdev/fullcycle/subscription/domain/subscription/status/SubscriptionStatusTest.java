package br.com.jkavdev.fullcycle.subscription.domain.subscription.status;

import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 1 - a factory
 * 2 - o metodo value()
 * 3 - transicao de status
 */
class SubscriptionStatusTest {

    @Test
    public void givenUnexpectedStatus_whenCallsCreate_shouldThrowError() {
        // given
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        );
        final var expectedStatus = "a";
        final var expectedErrorMessage = "invalid status :: a";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                SubscriptionStatus.create(expectedStatus, expectedSubscription));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullStatus_whenCallsCreate_shouldThrowError() {
        // given
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        );
        final String expectedStatus = null;
        final var expectedErrorMessage = "'status' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                SubscriptionStatus.create(expectedStatus, expectedSubscription));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullSubscription_whenCallsCreate_shouldThrowError() {
        // given
        final Subscription expectedSubscription = null;
        final var expectedStatus = "trailing";
        final var expectedErrorMessage = "'subscription' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                SubscriptionStatus.create(expectedStatus, expectedSubscription));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenActiveStatus_whenCallsCreate_should_InstantiateActiveSubscriptionStatus() {
        // given
        final var expectedStatus = "active";
        final var expectedStatusClass = ActiveSubscriptionStatus.class;
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        );

        // when
        final var actualStatus = SubscriptionStatus.create(expectedStatus, expectedSubscription);

        // then
        Assertions.assertEquals(expectedStatus, actualStatus.value());
        Assertions.assertEquals(expectedStatusClass, actualStatus.getClass());
    }

    @Test
    public void givenCanceledStatus_whenCallsCreate_should_InstantiateCanceledSubscriptionStatus() {
        // given
        final var expectedStatus = "canceled";
        final var expectedStatusClass = CanceledSubscriptionStatus.class;
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        );

        // when
        final var actualStatus = SubscriptionStatus.create(expectedStatus, expectedSubscription);

        // then
        Assertions.assertEquals(expectedStatus, actualStatus.value());
        Assertions.assertEquals(expectedStatusClass, actualStatus.getClass());
    }

    @Test
    public void givenIncompleteStatus_whenCallsCreate_should_InstantiateIncompleteSubscriptionStatus() {
        // given
        final var expectedStatus = "incomplete";
        final var expectedStatusClass = IncompleteSubscriptionStatus.class;
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        );

        // when
        final var actualStatus = SubscriptionStatus.create(expectedStatus, expectedSubscription);

        // then
        Assertions.assertEquals(expectedStatus, actualStatus.value());
        Assertions.assertEquals(expectedStatusClass, actualStatus.getClass());
    }

    @Test
    public void givenTrailingStatus_whenCallsCreate_should_InstantiateTrailingSubscriptionStatus() {
        // given
        final var expectedStatus = "trailing";
        final var expectedStatusClass = TrailingSubscriptionStatus.class;
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        );

        // when
        final var actualStatus = SubscriptionStatus.create(expectedStatus, expectedSubscription);

        // then
        Assertions.assertEquals(expectedStatus, actualStatus.value());
        Assertions.assertEquals(expectedStatusClass, actualStatus.getClass());
    }

}