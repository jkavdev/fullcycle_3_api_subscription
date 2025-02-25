package br.com.jkavdev.fullcycle.subscription.domain.subscription;

import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

class SubscriptionIncompleteTest {

    @Test
    public void givenValidSubscription_whenInstantiate_shouldReturnEvent() {
        // given
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                Fixture.Plans.plus()
        );
        expectedSubscription.status().incomplete();

        final var expectedReason = "qualquerRazao";

        // when
        final var actualEvent = new SubscriptionIncomplete(expectedSubscription, expectedReason);

        // then
        Assertions.assertEquals(expectedSubscription.id().value(), actualEvent.subscriptionId());
        Assertions.assertEquals(expectedSubscription.accountId().value(), actualEvent.accountId());
        Assertions.assertEquals(expectedSubscription.planId().value(), actualEvent.planId());
        Assertions.assertEquals(expectedReason, actualEvent.aReason());
        Assertions.assertEquals(expectedSubscription.dueDate(), actualEvent.dueDate());
        Assertions.assertNotNull(actualEvent.occurredOn());
    }

    @Test
    public void givenEmptySubscriptionId_whenInstantiate_shouldReturnError() {
        // given
        final String expectedSubscriptionId = null;
        final var expectedAccountId = "123";
        final var expectedPlanId = 123L;
        final var expectedDueDate = LocalDate.now();
        final var expectedOccurredOn = InstantUtils.now();
        final var expectedReason = "qualquerRazao";

        final var expectedErrorMessage = "'subscriptionId' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionIncomplete(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedReason,
                        expectedDueDate,
                        expectedOccurredOn
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenEmptyAccountId_whenInstantiate_shouldReturnError() {
        // given
        final var expectedSubscriptionId = "123";
        final String expectedAccountId = " ";
        final var expectedPlanId = 123L;
        final var expectedDueDate = LocalDate.now();
        final var expectedOccurredOn = InstantUtils.now();
        final var expectedReason = "qualquerRazao";

        final var expectedErrorMessage = "'accountId' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionIncomplete(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedReason,
                        expectedDueDate,
                        expectedOccurredOn
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullPlanId_whenInstantiate_shouldReturnError() {
        // given
        final var expectedSubscriptionId = "123";
        final var expectedAccountId = "123";
        final Long expectedPlanId = null;
        final var expectedDueDate = LocalDate.now();
        final var expectedOccurredOn = InstantUtils.now();
        final var expectedReason = "qualquerRazao";

        final var expectedErrorMessage = "'planId' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionIncomplete(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedReason,
                        expectedDueDate,
                        expectedOccurredOn
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenEmptyReason_whenInstantiate_shouldReturnError() {
        // given
        final var expectedSubscriptionId = "123";
        final String expectedAccountId = "123";
        final var expectedPlanId = 123L;
        final var expectedDueDate = LocalDate.now();
        final var expectedOccurredOn = InstantUtils.now();
        final var expectedReason = " ";

        final var expectedErrorMessage = "'reason' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionIncomplete(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedReason,
                        expectedDueDate,
                        expectedOccurredOn
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullDueDate_whenInstantiate_shouldReturnError() {
        // given
        final var expectedSubscriptionId = "123";
        final var expectedAccountId = "123";
        final var expectedPlanId = 123L;
        final LocalDate expectedDueDate = null;
        final var expectedOccurredOn = InstantUtils.now();
        final var expectedReason = "qualquerRazao";

        final var expectedErrorMessage = "'dueDate' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionIncomplete(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedReason,
                        expectedDueDate,
                        expectedOccurredOn
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullOccurredOn_whenInstantiate_shouldReturnError() {
        // given
        final var expectedSubscriptionId = "123";
        final var expectedAccountId = "123";
        final var expectedPlanId = 123L;
        final var expectedDueDate = LocalDate.now();
        final Instant expectedOccurredOn = null;
        final var expectedReason = "qualquerRazao";

        final var expectedErrorMessage = "'occurredOn' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionIncomplete(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedReason,
                        expectedDueDate,
                        expectedOccurredOn
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}