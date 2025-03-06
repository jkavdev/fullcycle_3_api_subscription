package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.domain.account.Account;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;
import br.com.jkavdev.fullcycle.subscription.domain.utils.IdUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccountInMemoryRepository implements AccountGateway {

    private Map<String, Account> db = new ConcurrentHashMap<>();
    private Map<String, Account> userIdIndex = new ConcurrentHashMap<>();

    @Override
    public AccountId nextId() {
        return new AccountId(IdUtils.uniqueId());
    }

    @Override
    public Optional<Account> accountOfId(AccountId anId) {
        return Optional.ofNullable(db.get(anId.value()));
    }

    @Override
    public Optional<Account> accountOfUserId(UserId userId) {
        return Optional.ofNullable(userIdIndex.get(userId.value()));
    }

    @Override
    public Account save(Account anAccount) {
        db.put(anAccount.id().value(), anAccount);
        userIdIndex.put(anAccount.userId().value(), anAccount);
        return anAccount;
    }
}
