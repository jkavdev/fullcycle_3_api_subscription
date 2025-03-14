package br.com.jkavdev.fullcycle.subscription.application.account.impl;

import br.com.jkavdev.fullcycle.subscription.application.account.CreateAccount;
import br.com.jkavdev.fullcycle.subscription.domain.account.Account;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;
import br.com.jkavdev.fullcycle.subscription.domain.person.Document;
import br.com.jkavdev.fullcycle.subscription.domain.person.Email;
import br.com.jkavdev.fullcycle.subscription.domain.person.Name;

import java.util.Objects;

public class DefaultCreateAccount extends CreateAccount {

    private final AccountGateway accountGateway;

    public DefaultCreateAccount(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Output execute(final Input input) {
        if (input == null) {
            throw new IllegalArgumentException("input to DefaultCreateIdpUser cannot be null");
        }
        final var anUserAccount = newAccountWith(input);
        return new StdOutput(accountGateway.save(anUserAccount).id());
    }

    private Account newAccountWith(final Input input) {
        return Account.newAccount(
                new AccountId(input.accountId()),
                new UserId(input.userId()),
                new Email(input.email()),
                new Name(input.firstname(), input.lastname()),
                Document.create(input.documentNumber(), input.documentType())
        );
    }

    record StdOutput(AccountId accountId) implements Output {

    }

}
