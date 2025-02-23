package br.com.jkavdev.fullcycle.subscription.domain.plan;

public sealed interface PlanCommand {

    record ChangePlan(String name, String description, Boolean active) implements PlanCommand {

    }

    record InactivePlan() implements PlanCommand {

    }

    record ActivatePlan() implements PlanCommand {

    }

}
