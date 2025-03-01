package br.com.jkavdev.fullcycle.subscription.application.subscription;

import br.com.jkavdev.fullcycle.subscription.application.UseCase;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;

public abstract class CancelSubscription extends UseCase<CancelSubscription.Input, CancelSubscription.Output> {

    public interface Input {
        String accountId();

        String subscriptionId();
    }

    public interface Output {
        String subscriptionStatus();

        SubscriptionId subscriptionId();
    }

}
