package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
import br.com.jkavdev.fullcycle.subscription.domain.utils.IdUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//@Component
public class SubscriptionInMemoryRepository implements SubscriptionGateway {

    private Map<String, Subscription> db = new ConcurrentHashMap<>();
    private Map<String, Set<Subscription>> accountIndex = new ConcurrentHashMap<>();

    @Override
    public SubscriptionId nextId() {
        return new SubscriptionId(IdUtils.uniqueId());
    }

    @Override
    public Optional<Subscription> subscriptionOfId(SubscriptionId anId) {
        return Optional.ofNullable(db.get(anId.value()));
    }

    @Override
    public Subscription save(Subscription subscription) {
        db.put(subscription.id().value(), subscription);
        updateAccountIndex(subscription);
        return subscription;
    }

    private void updateAccountIndex(Subscription subscription) {
        final var bag = accountIndex.getOrDefault(subscription.accountId().value(), new HashSet<>());
        bag.add(subscription);
        accountIndex.put(subscription.accountId().value(), bag);
    }

    @Override
    public Optional<Subscription> latestSubscriptionOfAccount(AccountId accountId) {
        return accountIndex.getOrDefault(accountId.value(), Set.of()).stream()
                .findFirst();
    }
}
