package br.com.jkavdev.fullcycle.subscription.domain.subscription;

import java.util.Optional;

public interface SubscriptionGateway {

    Optional<Subscription> subscriptionOfId(SubscriptionId anId);

    Subscription save(Subscription subscription);

}
