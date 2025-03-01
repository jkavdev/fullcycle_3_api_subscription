package br.com.jkavdev.fullcycle.subscription.application.subscription.impl;

import br.com.jkavdev.fullcycle.subscription.application.subscription.CancelSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.Fixture;
import br.com.jkavdev.fullcycle.subscription.domain.UnitTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.plan.Plan;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.status.ActiveSubscriptionStatus;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.status.CanceledSubscriptionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

// TODO: implementar o restante dos testes unitarios

class DefaultCancelSubscriptionTest extends UnitTest {

    @InjectMocks
    private DefaultCancelSubscription target;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @Captor
    ArgumentCaptor<Subscription> captor;

    @Test
    public void givenActiveSubscription_whenCallsCancelSubscription_shouldCancelIt() {
        // given
        final var expectedPlan = Fixture.Plans.plus();
        final var expectedAccount = Fixture.Accounts.jhou();
        final var expectedSubscription =
                newSubscriptionWith(
                        expectedAccount.id(),
                        expectedPlan,
                        ActiveSubscriptionStatus.ACTIVE,
                        LocalDateTime.now()
                );
        final var expectedSubscriptionId = expectedSubscription.id();
        final var expectedSubscriptionStatus = CanceledSubscriptionStatus.CANCELED;

        Mockito.when(subscriptionGateway.subscriptionOfId(ArgumentMatchers.any()))
                .thenReturn(Optional.of(expectedSubscription));
        Mockito.when(subscriptionGateway.save(ArgumentMatchers.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var actualOutput = target.execute(
                new CancelSubscriptionTestInput(expectedAccount.id().value(), expectedSubscriptionId.value())
        );

        // then
        Assertions.assertEquals(expectedSubscriptionId, actualOutput.subscriptionId());

        Mockito.verify(subscriptionGateway, Mockito.times(1))
                .save(captor.capture());

        final var actualSubscription = captor.getValue();
        Assertions.assertEquals(expectedSubscriptionId, actualSubscription.id());
        Assertions.assertEquals(expectedSubscriptionStatus, actualSubscription.status().value());
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

    record CancelSubscriptionTestInput(
            String accountId,
            String subscriptionId
    ) implements CancelSubscription.Input {
    }

}