package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res;

import br.com.jkavdev.fullcycle.subscription.application.subscription.CreateSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.AssertionConcern;

public record CreateSubscriptionResponse(String subscriptionId) implements AssertionConcern {

    public CreateSubscriptionResponse {
        this.assertArgumentNotNull(subscriptionId, "CreateSubscriptionResponse 'subscriptionId' should not be null");
    }

    public CreateSubscriptionResponse(final CreateSubscription.Output output) {
        this(output.subscriptionId().value());
    }

}
