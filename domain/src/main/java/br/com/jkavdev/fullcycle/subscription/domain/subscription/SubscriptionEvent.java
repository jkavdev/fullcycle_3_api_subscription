package br.com.jkavdev.fullcycle.subscription.domain.subscription;

import br.com.jkavdev.fullcycle.subscription.domain.DomainEvent;

public sealed interface SubscriptionEvent extends DomainEvent
        permits SubscriptionCreated,
        SubscriptionCanceled,
        SubscriptionIncomplete,
        SubscriptionRenewed {

    String TYPE = "Subscription";

    String subscriptionId();

    @Override
    default String aggregateId() {
        return subscriptionId();
    }

    @Override
    default String aggregateType() {
        return TYPE;
    }

}
