package br.com.jkavdev.fullcycle.subscription.application.subscription.impl;

import br.com.jkavdev.fullcycle.subscription.application.subscription.CancelSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionCommand;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;

import java.util.Objects;

public class DefaultCancelSubscription extends CancelSubscription {

    private final SubscriptionGateway subscriptionGateway;

    public DefaultCancelSubscription(
            final SubscriptionGateway subscriptionGateway
    ) {
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
    }

    @Override
    public Output execute(final Input input) {
        if (input == null) {
            throw new IllegalArgumentException("input to DefaultCancelSubscription cannot be null");
        }

        final var anAccountId = new AccountId(input.accountId());
        final var aSubscription = subscriptionGateway.latestSubscriptionOfAccount(anAccountId)
                .filter(it -> it.accountId().equals(anAccountId))
                .orElseThrow((() -> DomainException.with(
                        "subscription for account %s was not found".formatted(input.accountId())
                )));

        aSubscription.execute(new SubscriptionCommand.CancelSubscription());
        subscriptionGateway.save(aSubscription);

        return new StdOutput(aSubscription.status().value(), aSubscription.id());
    }

    public record StdOutput(
            String subscriptionStatus,
            SubscriptionId subscriptionId
    ) implements Output {

    }

}
