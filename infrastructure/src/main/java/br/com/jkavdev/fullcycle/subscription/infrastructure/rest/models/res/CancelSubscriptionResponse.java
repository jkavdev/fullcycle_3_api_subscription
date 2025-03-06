package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res;

import br.com.jkavdev.fullcycle.subscription.application.subscription.CancelSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.AssertionConcern;

public record CancelSubscriptionResponse(
        String subscriptionStatus,
        String subscriptionId
) implements AssertionConcern {

    public CancelSubscriptionResponse {
        this.assertArgumentNotEmpty(subscriptionStatus, "CreateSubscriptionResponse 'subscriptionStatus' should not be empty");
        this.assertArgumentNotNull(subscriptionId, "CreateSubscriptionResponse 'subscriptionId' should not be null");
    }

    public CancelSubscriptionResponse(final CancelSubscription.Output output) {
        this(output.subscriptionStatus(), output.subscriptionId().value());
    }

}
