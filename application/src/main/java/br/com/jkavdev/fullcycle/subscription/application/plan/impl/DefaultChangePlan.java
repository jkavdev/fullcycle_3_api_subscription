package br.com.jkavdev.fullcycle.subscription.application.plan.impl;

import br.com.jkavdev.fullcycle.subscription.application.plan.ChangePlan;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.money.Money;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanCommand;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanGateway;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;

import java.util.Objects;

public class DefaultChangePlan extends ChangePlan {

    private final PlanGateway planGateway;

    public DefaultChangePlan(final PlanGateway planGateway) {
        this.planGateway = Objects.requireNonNull(planGateway);
    }

    @Override
    public Output execute(final Input input) {
        final var aPlan = planGateway.planOfId(new PlanId(input.planId()))
                .orElseThrow(() -> DomainException.with("plan with id %s could not be found".formatted(input.planId())));

        aPlan.execute(
                new PlanCommand.ChangePlan(
                        input.name(),
                        input.description(),
                        new Money(input.currency(), input.price()),
                        input.active()
                )
        );
        planGateway.save(aPlan);

        return new StdOutput(aPlan.id());
    }

    record StdOutput(PlanId planId) implements Output {

    }

}
