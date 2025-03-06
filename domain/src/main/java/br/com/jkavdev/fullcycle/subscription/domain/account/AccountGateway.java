package br.com.jkavdev.fullcycle.subscription.domain.account;

import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;

import java.util.Optional;

public interface AccountGateway {

    AccountId nextId();

    Optional<Account> accountOfId(AccountId anId);

    Optional<Account> accountOfUserId(UserId userId);

    Account save(Account anAccount);

}
