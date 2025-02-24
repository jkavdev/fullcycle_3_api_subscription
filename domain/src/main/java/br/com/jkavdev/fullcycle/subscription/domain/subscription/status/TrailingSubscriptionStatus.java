package br.com.jkavdev.fullcycle.subscription.domain.subscription.status;

import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionCommand.ChangeStatus;

public record TrailingSubscriptionStatus(Subscription subscription) implements SubscriptionStatus {

    @Override
    public void trailing() {
        // nao faz nada
    }

    @Override
    public void incomplete() {
        subscription.execute(new ChangeStatus(new IncompleteSubscriptionStatus(subscription)));
    }

    @Override
    public void active() {
        subscription.execute(new ChangeStatus(new ActiveSubscriptionStatus(subscription)));
    }

    @Override
    public void cancel() {
        subscription.execute(new ChangeStatus(new CanceledSubscriptionStatus(subscription)));
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
