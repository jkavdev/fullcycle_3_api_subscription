package br.com.jkavdev.fullcycle.subscription.domain.subscription;

import br.com.jkavdev.fullcycle.subscription.domain.subscription.status.SubscriptionStatus;

public sealed interface SubscriptionCommand {

    record ChangeStatus(SubscriptionStatus status) implements SubscriptionCommand {

    }

}
