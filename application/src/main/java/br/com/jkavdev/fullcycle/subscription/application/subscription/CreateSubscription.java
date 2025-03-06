package br.com.jkavdev.fullcycle.subscription.application.subscription;

import br.com.jkavdev.fullcycle.subscription.application.UseCase;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;

public abstract class CreateSubscription extends UseCase<CreateSubscription.Input, CreateSubscription.Output> {

    public interface Input {
        String accountId();

        Long planId();
    }

    public interface Output {
        SubscriptionId subscriptionId();
    }

}
