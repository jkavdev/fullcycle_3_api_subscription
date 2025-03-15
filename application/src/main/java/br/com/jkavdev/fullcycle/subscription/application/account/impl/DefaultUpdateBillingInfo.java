package br.com.jkavdev.fullcycle.subscription.application.account.impl;

import br.com.jkavdev.fullcycle.subscription.application.account.UpdateBillingInfo;
import br.com.jkavdev.fullcycle.subscription.domain.account.Account;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountCommand.ChangeProfileCommand;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.person.Address;

import java.util.Objects;

public class DefaultUpdateBillingInfo extends UpdateBillingInfo {

    private final AccountGateway accountGateway;

    public DefaultUpdateBillingInfo(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Output execute(final Input input) {
        if (input == null) {
            throw new IllegalArgumentException("input to DefaultUpdateBillingInfo cannot be null");
        }
        final var anAccountId = new AccountId(input.accountId());
        final var anAccount = accountGateway.accountOfId(anAccountId)
                .orElseThrow(() -> DomainException.notFound(Account.class, anAccountId));

        anAccount.execute(new ChangeProfileCommand(anAccount.name(), newBillingAddress(input)));
        accountGateway.save(anAccount);

        return new StdOutput(anAccountId);
    }

    private Address newBillingAddress(final Input input) {
        return new Address(
                input.zipcode(),
                input.number(),
                input.complement(),
                input.country()
        );
    }

    record StdOutput(AccountId accountId) implements UpdateBillingInfo.Output {

    }

}
