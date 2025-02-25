package br.com.jkavdev.fullcycle.subscription.domain.subscription;

import br.com.jkavdev.fullcycle.subscription.domain.utils.InstantUtils;

import java.time.Instant;
import java.time.LocalDate;

public record SubscriptionIncomplete(
        String subscriptionId,
        String accountId,
        Long planId,
        String aReason,
        LocalDate dueDate,
        Instant occurredOn
) implements SubscriptionEvent {

    public SubscriptionIncomplete {
        this.assertArgumentNotEmpty(subscriptionId, "'subscriptionId' should not be empty");
        this.assertArgumentNotEmpty(accountId, "'accountId' should not be empty");
        this.assertArgumentNotNull(planId, "'planId' should not be null");
        this.assertArgumentNotEmpty(aReason, "'reason' should not be empty");
        this.assertArgumentNotNull(dueDate, "'dueDate' should not be null");
        this.assertArgumentNotNull(occurredOn, "'occurredOn' should not be null");
    }

    public SubscriptionIncomplete(final Subscription aSubscription, final String aReason) {
        this(
                aSubscription.id().value(),
                aSubscription.accountId().value(),
                aSubscription.planId().value(),
                aReason,
                aSubscription.dueDate(),
                InstantUtils.now()
        );
    }

}
