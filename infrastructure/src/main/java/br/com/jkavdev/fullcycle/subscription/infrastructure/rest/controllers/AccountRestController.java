package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.controllers;

import br.com.jkavdev.fullcycle.subscription.application.account.UpdateBillingInfo;
import br.com.jkavdev.fullcycle.subscription.infrastructure.authentication.principal.CodeflixUser;
import br.com.jkavdev.fullcycle.subscription.infrastructure.mediator.SignUpMediator;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.AccountRestApi;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req.BillingInfoRequest;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req.SignUpRequest;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.BillingInfoResponse;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.SignUpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class AccountRestController implements AccountRestApi {

    private final SignUpMediator signUpMediator;

    private final UpdateBillingInfo updateBillingInfo;

    public AccountRestController(
            final SignUpMediator signUpMediator,
            final UpdateBillingInfo updateBillingInfo
    ) {
        this.signUpMediator = Objects.requireNonNull(signUpMediator);
        this.updateBillingInfo = Objects.requireNonNull(updateBillingInfo);
    }

    @Override
    public ResponseEntity<SignUpResponse> signUp(final SignUpRequest req) {
        final var res = signUpMediator.signUp(req);
        return ResponseEntity
                .created(URI.create("/accounts/%s".formatted(res.accountId())))
                .body(res);
    }

    @Override
    public ResponseEntity<BillingInfoResponse> updateBillingInfo(
            final CodeflixUser principal,
            final BillingInfoRequest req
    ) {
        record Input(
                String accountId,
                String zipcode,
                String number,
                String complement,
                String country
        ) implements UpdateBillingInfo.Input {
        }

        final var input = new Input(
                principal.accountId(),
                req.zipcode(),
                req.number(),
                req.complement(),
                req.country()
        );
        final var output = updateBillingInfo.execute(input);

        return ResponseEntity.accepted()
                .body(new BillingInfoResponse(output.accountId().value()));
    }

}
