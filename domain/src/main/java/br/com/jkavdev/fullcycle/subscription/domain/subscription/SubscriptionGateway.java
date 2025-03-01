package br.com.jkavdev.fullcycle.subscription.domain.subscription;

import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;

import java.util.Optional;

public interface SubscriptionGateway {

    SubscriptionId nextId();

    Optional<Subscription> subscriptionOfId(SubscriptionId anId);

    Subscription save(Subscription subscription);

    Optional<Subscription> latestSubscriptionOfAccount(AccountId accountId);

}
