package br.com.jkavdev.fullcycle.subscription.infrastructure.mediator;

import br.com.jkavdev.fullcycle.subscription.application.account.CreateAccount;
import br.com.jkavdev.fullcycle.subscription.application.account.CreateIdpUser;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req.SignUpRequest;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.SignUpResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SignUpMediator {

    private final CreateAccount createAccount;

    private final CreateIdpUser createIdpUser;

    public SignUpMediator(
            final CreateAccount createAccount,
            final CreateIdpUser createIdpUser
    ) {
        this.createAccount = Objects.requireNonNull(createAccount);
        this.createIdpUser = Objects.requireNonNull(createIdpUser);
    }

    public SignUpResponse signUp(final SignUpRequest req) {
        final var out = createIdpUser.execute(req);
        return createAccount.execute(new CreateAccountInput(req, out.idpUserId().value()), SignUpResponse::new);
    }

    record CreateAccountInput(SignUpRequest req, String userId) implements CreateAccount.Input {

        @Override
        public String firstname() {
            return req().firstname();
        }

        @Override
        public String lastname() {
            return req().lastname();
        }

        @Override
        public String email() {
            return req().email();
        }

        @Override
        public String documentNumber() {
            return req().documentNumber();
        }

        @Override
        public String documentType() {
            return req().documentType();
        }
    }

}
