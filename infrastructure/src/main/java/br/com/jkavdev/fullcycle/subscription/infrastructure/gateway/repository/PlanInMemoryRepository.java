package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.domain.plan.Plan;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanGateway;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

//@Component
public class PlanInMemoryRepository implements PlanGateway {

    private static final AtomicLong id = new AtomicLong(1);
    private Map<Long, Plan> db = new ConcurrentHashMap<>();

    @Override
    public PlanId nextId() {
        return new PlanId(id.getAndIncrement());
    }

    @Override
    public Optional<Plan> planOfId(PlanId anId) {
        return Optional.ofNullable(db.get(anId.value()));
    }

    @Override
    public List<Plan> allPlans() {
        return db.values().stream().toList();
    }

    @Override
    public boolean existsPlanOfId(PlanId anId) {
        return db.containsKey(anId.value());
    }

    @Override
    public Plan save(Plan plan) {
        db.put(plan.id().value(), plan);
        return plan;
    }
}
