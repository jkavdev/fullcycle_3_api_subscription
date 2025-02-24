package br.com.jkavdev.fullcycle.subscription.domain.subscription.status;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;

public record CanceledSubscriptionStatus(Subscription subscription) implements SubscriptionStatus {

    @Override
    public void trailing() {
        throw DomainException.with("subscription with status canceled can't transit to trailing");
    }

    @Override
    public void incomplete() {
        throw DomainException.with("subscription with status canceled can't transit to incomplete");
    }

    @Override
    public void active() {
        throw DomainException.with("subscription with status canceled can't transit to active");
    }

    @Override
    public void cancel() {
        // nao faz nada
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
