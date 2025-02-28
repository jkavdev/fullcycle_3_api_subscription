package br.com.jkavdev.fullcycle.subscription.domain.plan;

import br.com.jkavdev.fullcycle.subscription.domain.money.Money;

public sealed interface PlanCommand {

    record ChangePlan(
            String name,
            String description,
            Money money,
            Boolean active
    ) implements PlanCommand {

    }

    record InactivePlan() implements PlanCommand {

    }

    record ActivatePlan() implements PlanCommand {

    }

}
