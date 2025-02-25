package br.com.jkavdev.fullcycle.subscription.domain.subscription;

import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

class SubscriptionRenewedTest {

    @Test
    public void givenValidSubscription_whenInstantiate_shouldReturnEvent() {
        // given
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedSubscription = Subscription.newSubscription(
                new SubscriptionId("qualquerId"),
                new AccountId("qualquerId"),
                expectedPlan
        );
        final var expectedLastTransactionId = UUID.randomUUID().toString();
        expectedSubscription.execute(new SubscriptionCommand.RenewSubscription(expectedPlan, expectedLastTransactionId));

        // when
        final var actualEvent = new SubscriptionRenewed(expectedSubscription, expectedPlan);

        // then
        Assertions.assertEquals(expectedSubscription.id().value(), actualEvent.subscriptionId());
        Assertions.assertEquals(expectedSubscription.accountId().value(), actualEvent.accountId());
        Assertions.assertEquals(expectedSubscription.planId().value(), actualEvent.planId());
        Assertions.assertEquals(expectedSubscription.lastTransactionId(), actualEvent.transactionId());
        Assertions.assertEquals(expectedSubscription.dueDate(), actualEvent.dueDate());
        Assertions.assertEquals(expectedPlan.price().currency().getCurrencyCode(), actualEvent.currency());
        Assertions.assertEquals(expectedPlan.price().amount(), actualEvent.amount());
        Assertions.assertEquals(expectedSubscription.lastRenewDate(), actualEvent.renewedAt());
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
        final var expectedCurrency = "qualquerMoeda";
        final var expectedTransactionId = "qualquerTransactionId";
        final var expectedAmount = 10.00;
        final var expectedRenewedAt = InstantUtils.now();

        final var expectedErrorMessage = "'subscriptionId' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionRenewed(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedTransactionId,
                        expectedDueDate,
                        expectedCurrency,
                        expectedAmount,
                        expectedRenewedAt,
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
        final var expectedCurrency = "qualquerMoeda";
        final var expectedTransactionId = "qualquerTransactionId";
        final var expectedAmount = 10.00;
        final var expectedRenewedAt = InstantUtils.now();

        final var expectedErrorMessage = "'accountId' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionRenewed(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedTransactionId,
                        expectedDueDate,
                        expectedCurrency,
                        expectedAmount,
                        expectedRenewedAt,
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
        final var expectedCurrency = "qualquerMoeda";
        final var expectedTransactionId = "qualquerTransactionId";
        final var expectedAmount = 10.00;
        final var expectedRenewedAt = InstantUtils.now();

        final var expectedErrorMessage = "'planId' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionRenewed(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedTransactionId,
                        expectedDueDate,
                        expectedCurrency,
                        expectedAmount,
                        expectedRenewedAt,
                        expectedOccurredOn
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenEmptyTransactionId_whenInstantiate_shouldReturnError() {
        // given
        final var expectedSubscriptionId = "123";
        final var expectedAccountId = "123";
        final var expectedPlanId = 123L;
        final var expectedDueDate = LocalDate.now();
        final var expectedOccurredOn = InstantUtils.now();
        final var expectedCurrency = "qualquerMoeda";
        final var expectedTransactionId = "  ";
        final var expectedAmount = 10.00;
        final var expectedRenewedAt = InstantUtils.now();

        final var expectedErrorMessage = "'transactionId' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionRenewed(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedTransactionId,
                        expectedDueDate,
                        expectedCurrency,
                        expectedAmount,
                        expectedRenewedAt,
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
        final var expectedCurrency = "qualquerMoeda";
        final var expectedTransactionId = "qualquerTransactionId";
        final var expectedAmount = 10.00;
        final var expectedRenewedAt = InstantUtils.now();

        final var expectedErrorMessage = "'dueDate' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionRenewed(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedTransactionId,
                        expectedDueDate,
                        expectedCurrency,
                        expectedAmount,
                        expectedRenewedAt,
                        expectedOccurredOn
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenEmptyCurrency_whenInstantiate_shouldReturnError() {
        // given
        final var expectedSubscriptionId = "123";
        final var expectedAccountId = "123";
        final var expectedPlanId = 123L;
        final var expectedDueDate = LocalDate.now();
        final var expectedOccurredOn = InstantUtils.now();
        final var expectedCurrency = "  ";
        final var expectedTransactionId = "qualquerTransactionId";
        final var expectedAmount = 10.00;
        final var expectedRenewedAt = InstantUtils.now();

        final var expectedErrorMessage = "'currency' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionRenewed(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedTransactionId,
                        expectedDueDate,
                        expectedCurrency,
                        expectedAmount,
                        expectedRenewedAt,
                        expectedOccurredOn
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullAmount_whenInstantiate_shouldReturnError() {
        // given
        final var expectedSubscriptionId = "123";
        final var expectedAccountId = "123";
        final var expectedPlanId = 123L;
        final var expectedDueDate = LocalDate.now();
        final var expectedOccurredOn = InstantUtils.now();
        final var expectedCurrency = "qualquerMoeda";
        final var expectedTransactionId = "qualquerTransactionId";
        final Double expectedAmount = null;
        final var expectedRenewedAt = InstantUtils.now();

        final var expectedErrorMessage = "'amount' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionRenewed(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedTransactionId,
                        expectedDueDate,
                        expectedCurrency,
                        expectedAmount,
                        expectedRenewedAt,
                        expectedOccurredOn
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullRenewedAt_whenInstantiate_shouldReturnError() {
        // given
        final var expectedSubscriptionId = "123";
        final var expectedAccountId = "123";
        final var expectedPlanId = 123L;
        final var expectedDueDate = LocalDate.now();
        final var expectedOccurredOn = InstantUtils.now();
        final var expectedCurrency = "qualquerMoeda";
        final var expectedTransactionId = "qualquerTransactionId";
        final var expectedAmount = 10.00;
        final Instant expectedRenewedAt = null;

        final var expectedErrorMessage = "'renewedAt' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionRenewed(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedTransactionId,
                        expectedDueDate,
                        expectedCurrency,
                        expectedAmount,
                        expectedRenewedAt,
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
        final var expectedCurrency = "qualquerMoeda";
        final var expectedTransactionId = "qualquerTransactionId";
        final var expectedAmount = 10.00;
        final var expectedRenewedAt = InstantUtils.now();

        final var expectedErrorMessage = "'occurredOn' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new SubscriptionRenewed(
                        expectedSubscriptionId,
                        expectedAccountId,
                        expectedPlanId,
                        expectedTransactionId,
                        expectedDueDate,
                        expectedCurrency,
                        expectedAmount,
                        expectedRenewedAt,
                        expectedOccurredOn
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}