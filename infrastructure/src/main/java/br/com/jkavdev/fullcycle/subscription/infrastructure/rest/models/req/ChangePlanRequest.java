package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req;

import br.com.jkavdev.fullcycle.subscription.application.plan.ChangePlan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangePlanRequest(
        @NotNull Long planId,
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 1000) String description,
        @NotNull Double price,
        @NotBlank @Size(min = 3, max = 3) String currency,
        Boolean active
) implements ChangePlan.Input {
}
