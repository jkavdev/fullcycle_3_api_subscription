package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.controllers;

import br.com.jkavdev.fullcycle.subscription.application.subscription.CreateSubscription;
import br.com.jkavdev.fullcycle.subscription.infrastructure.authentication.principal.CodeflixUser;
import br.com.jkavdev.fullcycle.subscription.infrastructure.authentication.principal.CodelixAuthentication;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.SubscriptionRestApi;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req.CreateSubscriptionRequest;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.CreateSubscriptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.Objects;

@RestController
public class SubscriptionRestController implements SubscriptionRestApi {

    private final CreateSubscription createSubscription;

    public SubscriptionRestController(
            final CreateSubscription createSubscription
    ) {
        this.createSubscription = Objects.requireNonNull(createSubscription);
    }

    @Override
    public ResponseEntity<CreateSubscriptionResponse> createSubscription(
            final CreateSubscriptionRequest req,
            final CodeflixUser principal
    ) {
        record CreateSubscriptionInput(Long planId, String accountId) implements CreateSubscription.Input {

        }
        final var input = new CreateSubscriptionInput(req.planId(), principal.accountId());
        final var res = createSubscription.execute(input, CreateSubscriptionResponse::new);
        return ResponseEntity
                .created(URI.create("/subscriptions/%s".formatted(res.subscriptionId())))
                .body(res);
    }

}
