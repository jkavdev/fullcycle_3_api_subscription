package br.com.jkavdev.fullcycle.subscription.application.plan.impl;

import br.com.jkavdev.fullcycle.subscription.application.plan.CreatePlan;
import br.com.jkavdev.fullcycle.subscription.domain.money.Money;
import br.com.jkavdev.fullcycle.subscription.domain.plan.Plan;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanGateway;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;

import java.util.Objects;

public class DefaultCreatePlan extends CreatePlan {

    private final PlanGateway planGateway;

    public DefaultCreatePlan(final PlanGateway planGateway) {
        this.planGateway = Objects.requireNonNull(planGateway);
    }

    @Override
    public Output execute(final Input input) {
        final var aPlan = newPlanWith(input);
        planGateway.save(aPlan);
        return new StdOutput(aPlan.id());
    }

    private Plan newPlanWith(final Input input) {
        return Plan.newPlan(
                planGateway.nextId(),
                input.name(),
                input.description(),
                input.active(),
                new Money(input.currency(), input.price())
        );
    }

    record StdOutput(PlanId planId) implements Output {

    }

}
