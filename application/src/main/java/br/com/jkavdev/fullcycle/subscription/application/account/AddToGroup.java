package br.com.jkavdev.fullcycle.subscription.application.account;

import br.com.jkavdev.fullcycle.subscription.application.UseCase;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;

public abstract class AddToGroup extends UseCase<AddToGroup.Input, AddToGroup.Output> {

    public interface Input {
        String accountId();

        String groupId();

        String subscriptionId();
    }

    public interface Output {
        SubscriptionId subscriptionId();
    }

}
