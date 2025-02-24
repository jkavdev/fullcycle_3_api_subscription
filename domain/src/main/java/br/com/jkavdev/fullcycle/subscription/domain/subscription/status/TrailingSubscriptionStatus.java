package br.com.jkavdev.fullcycle.subscription.domain.subscription.status;

import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;

public record TrailingSubscriptionStatus(Subscription subscription) implements SubscriptionStatus {

    @Override
    public String value() {
        return TRAILING;
    }

    @Override
    public void trailing() {
        // nao faz nada
    }

    @Override
    public void incomplete() {

    }

    @Override
    public void active() {

    }

    @Override
    public void cancel() {

    }

}
