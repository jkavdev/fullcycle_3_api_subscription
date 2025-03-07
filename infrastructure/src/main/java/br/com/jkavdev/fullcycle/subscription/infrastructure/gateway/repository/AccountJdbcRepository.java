package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.domain.account.Account;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
public class AccountJdbcRepository implements AccountGateway {

    private final JdbcClient jdbcClient;

    public AccountJdbcRepository(final JdbcClient jdbcClient) {
        this.jdbcClient = Objects.requireNonNull(jdbcClient);
    }

    @Override
    public AccountId nextId() {
        return null;
    }

    @Override
    public Optional<Account> accountOfId(AccountId anId) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> accountOfUserId(UserId userId) {
        return Optional.empty();
    }

    @Override
    public Account save(Account anAccount) {
        return null;
    }
}
