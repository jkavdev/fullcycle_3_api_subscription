package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req;

import org.springframework.lang.NonNull;

public record CreateSubscriptionRequest(
        @NonNull Long planId
) {
}
