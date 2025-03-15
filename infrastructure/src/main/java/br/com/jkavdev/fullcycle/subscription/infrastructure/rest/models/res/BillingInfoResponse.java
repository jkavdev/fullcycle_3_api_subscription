package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res;

import br.com.jkavdev.fullcycle.subscription.application.account.UpdateBillingInfo;
import br.com.jkavdev.fullcycle.subscription.domain.AssertionConcern;

public record BillingInfoResponse(String accountId) implements AssertionConcern {

    public BillingInfoResponse {
        this.assertArgumentNotEmpty(accountId, "'accountId' must not be empty");
    }

    public BillingInfoResponse(final UpdateBillingInfo.Output output) {
        this(output.accountId().value());
    }

}
