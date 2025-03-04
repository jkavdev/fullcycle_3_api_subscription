package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.controllers;

import br.com.jkavdev.fullcycle.subscription.application.plan.CreatePlan;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.PlanRestApi;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req.CreatePlanRequest;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.CreatePlanResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class PlanRestController implements PlanRestApi {

    private final CreatePlan createPlan;

    public PlanRestController(final CreatePlan createPlan) {
        this.createPlan = Objects.requireNonNull(createPlan);
    }

    @Override
    public ResponseEntity<CreatePlanResponse> createPlan(final CreatePlanRequest req) {
        final var res = createPlan.execute(req, CreatePlanResponse::new);
        return ResponseEntity
                .created(URI.create("/plans/%s".formatted(res.planId())))
                .body(res);
    }

}
