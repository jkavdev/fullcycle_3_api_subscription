package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req;

import jakarta.validation.constraints.NotNull;

public record CreateSubscriptionRequest(
        @NotNull Long planId
) {
}
