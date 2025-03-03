package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res;

import br.com.jkavdev.fullcycle.subscription.application.account.CreateAccount;
import br.com.jkavdev.fullcycle.subscription.domain.AssertionConcern;

public record SignUpResponse(String accountId) implements AssertionConcern {

    public SignUpResponse {
        this.assertArgumentNotEmpty(accountId, "'accountId' must not be empty");
    }

    public SignUpResponse(final CreateAccount.Output output) {
        this(output.accountId().value());
    }

}
