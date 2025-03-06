package br.com.jkavdev.fullcycle.subscription.infrastructure.authentication.principal;

import br.com.jkavdev.fullcycle.subscription.domain.account.Account;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;

import java.util.Optional;
import java.util.function.Function;

public interface AccountFromUserIdResolver extends Function<UserId, Optional<Account>> {
}
