package br.com.jkavdev.fullcycle.subscription.application.plan;

import br.com.jkavdev.fullcycle.subscription.application.UseCase;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;

public abstract class ChangePlan extends UseCase<ChangePlan.Input, ChangePlan.Output> {

    public interface Input {
        Long planId();

        String name();

        String description();

        String currency();

        Double price();

        Boolean active();
    }

    public interface Output {
        PlanId planId();
    }

}
