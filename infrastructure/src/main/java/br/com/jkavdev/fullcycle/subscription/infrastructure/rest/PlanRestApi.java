package br.com.jkavdev.fullcycle.subscription.infrastructure.rest;

import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req.CreatePlanRequest;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.CreatePlanResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("plans")
@Tag(name = "Plan")
public interface PlanRestApi {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new plan")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "created successfully"),
                    @ApiResponse(responseCode = "422", description = "a validation error was observed"),
                    @ApiResponse(responseCode = "500", description = "an unpredictable error was observed"),
            }
    )
    ResponseEntity<CreatePlanResponse> createPlan(@RequestBody @Valid CreatePlanRequest req);

}
