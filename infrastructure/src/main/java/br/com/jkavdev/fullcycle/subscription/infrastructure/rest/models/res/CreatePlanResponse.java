package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res;

import br.com.jkavdev.fullcycle.subscription.application.plan.CreatePlan;
import br.com.jkavdev.fullcycle.subscription.domain.AssertionConcern;

public record CreatePlanResponse(Long planId) implements AssertionConcern {

    public CreatePlanResponse {
        this.assertArgumentNotNull(planId, "CreatePlanResponse 'planId' should not be null");
    }

    public CreatePlanResponse(final CreatePlan.Output output) {
        this(output.planId().value());
    }

}
