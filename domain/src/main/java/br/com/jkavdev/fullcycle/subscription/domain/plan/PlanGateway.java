package br.com.jkavdev.fullcycle.subscription.domain.plan;

import java.util.List;
import java.util.Optional;

public interface PlanGateway {

    PlanId nextId();

    Optional<Plan> planOfId(PlanId anId);

    List<Plan> allPlans();

    boolean existsPlanOfId(PlanId anId);

    Plan save(Plan plan);

}
