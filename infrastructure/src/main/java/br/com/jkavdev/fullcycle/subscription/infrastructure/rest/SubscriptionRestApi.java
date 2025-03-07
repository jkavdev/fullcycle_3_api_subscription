package br.com.jkavdev.fullcycle.subscription.infrastructure.rest;

import br.com.jkavdev.fullcycle.subscription.infrastructure.authentication.principal.CodeflixUser;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req.ChargeSubscriptionRequest;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req.CreateSubscriptionRequest;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.CancelSubscriptionResponse;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.ChargeSubscriptionResponse;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.CreateSubscriptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("subscriptions")
@Tag(name = "Subscription")
public interface SubscriptionRestApi {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new subscription")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "created successfully"),
                    @ApiResponse(responseCode = "422", description = "a validation error was observed"),
                    @ApiResponse(responseCode = "500", description = "an unpredictable error was observed"),
            }
    )
    ResponseEntity<CreateSubscriptionResponse> createSubscription(
            @RequestBody @Valid CreateSubscriptionRequest req,
            @AuthenticationPrincipal final CodeflixUser principal
    );

    @PutMapping(
            value = "active/cancel",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "cancel an active subscription")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "canceled successfully"),
                    @ApiResponse(responseCode = "422", description = "a validation error was observed"), // TODO: acho que esse erro nao eh possivel aqui
                    @ApiResponse(responseCode = "500", description = "an unpredictable error was observed"),
            }
    )
    ResponseEntity<CancelSubscriptionResponse> cancelSubscription(
            @AuthenticationPrincipal final CodeflixUser principal
    );

    @PutMapping(
            value = "active/charge",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "charge an active subscription")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "charged successfully"),
                    @ApiResponse(responseCode = "422", description = "a validation error was observed"), // TODO: acho que esse erro nao eh possivel aqui
                    @ApiResponse(responseCode = "500", description = "an unpredictable error was observed"),
            }
    )
    ResponseEntity<ChargeSubscriptionResponse> chargeActiveSubscription(
            @RequestBody @Valid ChargeSubscriptionRequest req,
            @AuthenticationPrincipal final CodeflixUser principal
    );

}
