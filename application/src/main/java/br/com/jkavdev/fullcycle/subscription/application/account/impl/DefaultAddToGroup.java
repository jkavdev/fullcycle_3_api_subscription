package br.com.jkavdev.fullcycle.subscription.application.account.impl;

import br.com.jkavdev.fullcycle.subscription.application.account.AddToGroup;
import br.com.jkavdev.fullcycle.subscription.domain.account.Account;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.GroupId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;

import java.util.Objects;

public class DefaultAddToGroup extends AddToGroup {

    private final IdentityProviderGateway identityProviderGateway;

    private final SubscriptionGateway subscriptionGateway;

    private final AccountGateway accountGateway;

    public DefaultAddToGroup(
            final IdentityProviderGateway identityProviderGateway,
            final SubscriptionGateway subscriptionGateway,
            final AccountGateway accountGateway
    ) {
        this.identityProviderGateway = Objects.requireNonNull(identityProviderGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Output execute(final Input input) {
        if (input == null) {
            throw new IllegalArgumentException("input to DefaultAddToGroup cannot be null");
        }
        final var anAccountId = new AccountId(input.accountId());
        final var anSubscriptionId = new SubscriptionId(input.subscriptionId());
        final var aSubscription = subscriptionGateway.subscriptionOfId(anSubscriptionId)
                .filter(it -> it.accountId().equals(anAccountId))
                .orElseThrow(() -> DomainException.notFound(Subscription.class, anSubscriptionId));

        if (aSubscription.isTrail() || aSubscription.isActive()) {
            final var userId = accountGateway.accountOfId(anAccountId)
                    .orElseThrow(() -> DomainException.notFound(Account.class, anAccountId))
                    .userId();
            identityProviderGateway.addUserToGroup(userId, new GroupId(input.groupId()));
        }

        return new StdOutput(anSubscriptionId);
    }

    record StdOutput(SubscriptionId subscriptionId) implements Output {

    }

}
