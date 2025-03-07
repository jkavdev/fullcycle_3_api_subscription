package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res;

import br.com.jkavdev.fullcycle.subscription.application.subscription.ChargeSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.AssertionConcern;

public record ChargeSubscriptionResponse(
        String subscriptionId,
        String subscriptionStatus,
        String subscriptionDueDate,
        String paymentTransactionId,
        String paymentTransactionError
) implements AssertionConcern {

    public ChargeSubscriptionResponse(final ChargeSubscription.Output output) {
        this(
                output.subscriptionId().value(),
                output.subscriptionStatus(),
                output.subscriptionDueDate().toString(),
                output.paymentTransaction().transactionId(),
                output.paymentTransaction().errorMessage()
        );
    }

}
