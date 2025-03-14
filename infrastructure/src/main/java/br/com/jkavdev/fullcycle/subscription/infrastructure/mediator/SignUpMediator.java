package br.com.jkavdev.fullcycle.subscription.infrastructure.mediator;

import br.com.jkavdev.fullcycle.subscription.application.account.CreateAccount;
import br.com.jkavdev.fullcycle.subscription.application.account.CreateIdpUser;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req.SignUpRequest;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.SignUpResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;

@Component
public class SignUpMediator {

    private final AccountGateway accountGateway;

    private final CreateAccount createAccount;

    private final CreateIdpUser createIdpUser;

    public SignUpMediator(
            final AccountGateway accountGateway,
            final CreateAccount createAccount,
            final CreateIdpUser createIdpUser
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.createAccount = Objects.requireNonNull(createAccount);
        this.createIdpUser = Objects.requireNonNull(createIdpUser);
    }

    public SignUpResponse signUp(final SignUpRequest req) {
        return nextAccountId()
                .andThen(createIdpUser())
                .andThen(createAccount())
                .apply(req);
    }

    private Function<SignUpRequest, SignUpRequest> nextAccountId() {
        return req -> req.with(accountGateway.nextId());
    }

    private Function<SignUpRequest, SignUpRequest> createIdpUser() {
        return req -> createIdpUser.execute(req, req::with);
    }

    private Function<SignUpRequest, SignUpResponse> createAccount() {
        return req -> createAccount.execute(req, SignUpResponse::new);
    }

}
