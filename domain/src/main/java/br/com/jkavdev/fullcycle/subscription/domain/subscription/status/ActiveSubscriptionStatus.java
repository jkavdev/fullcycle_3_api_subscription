package br.com.jkavdev.fullcycle.subscription.domain.subscription.status;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionCommand;

public record ActiveSubscriptionStatus(Subscription subscription) implements SubscriptionStatus {

    @Override
    public void trailing() {
        throw DomainException.with("subscription with status active can't transit to trailing");
    }

    @Override
    public void incomplete() {
        subscription.execute(new SubscriptionCommand.ChangeStatus(new IncompleteSubscriptionStatus(subscription)));
    }

    @Override
    public void active() {
        // nao faz nada
    }

    @Override
    public void cancel() {
        subscription.execute(new SubscriptionCommand.ChangeStatus(new CanceledSubscriptionStatus(subscription)));
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return value();
    }

}
