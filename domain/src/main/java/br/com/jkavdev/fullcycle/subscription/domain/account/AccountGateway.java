package br.com.jkavdev.fullcycle.subscription.domain.account;

import java.util.Optional;

public interface AccountGateway {

    AccountId nextId();

    Optional<Account> accountOfId(AccountId anId);

    Account save(Account anAccount);

}
