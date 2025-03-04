package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res;

import br.com.jkavdev.fullcycle.subscription.application.plan.ChangePlan;
import br.com.jkavdev.fullcycle.subscription.domain.AssertionConcern;

public record ChangePlanResponse(Long planId) implements AssertionConcern {

    public ChangePlanResponse {
        this.assertArgumentNotNull(planId, "ChangePlanResponse 'planId' should not be null");
    }

    public ChangePlanResponse(final ChangePlan.Output output) {
        this(output.planId().value());
    }

}
