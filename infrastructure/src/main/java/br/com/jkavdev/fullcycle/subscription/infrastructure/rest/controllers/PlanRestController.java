package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.controllers;

import br.com.jkavdev.fullcycle.subscription.application.plan.ChangePlan;
import br.com.jkavdev.fullcycle.subscription.application.plan.CreatePlan;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.PlanRestApi;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req.ChangePlanRequest;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req.CreatePlanRequest;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.ChangePlanResponse;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.CreatePlanResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class PlanRestController implements PlanRestApi {

    private final CreatePlan createPlan;

    private final ChangePlan changePlan;

    public PlanRestController(
            final CreatePlan createPlan,
            final ChangePlan changePlan
    ) {
        this.createPlan = Objects.requireNonNull(createPlan);
        this.changePlan = Objects.requireNonNull(changePlan);
    }

    @Override
    public ResponseEntity<CreatePlanResponse> createPlan(final CreatePlanRequest req) {
        final var res = createPlan.execute(req, CreatePlanResponse::new);
        return ResponseEntity
                .created(URI.create("/plans/%s".formatted(res.planId())))
                .body(res);
    }

    @Override
    public ResponseEntity<ChangePlanResponse> changePlan(
            final Long planId,
            final ChangePlanRequest req
    ) {
        if (!req.planId().equals(planId)) {
            throw DomainException.with("plan identifier does not matches");
        }
        final var res = changePlan.execute(req, ChangePlanResponse::new);
        return ResponseEntity
                .ok(res);
    }

}
