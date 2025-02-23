package br.com.jkavdev.fullcycle.subscription.domain.account;

import br.com.jkavdev.fullcycle.subscription.domain.Identifier;

public record AccountId(String value) implements Identifier {

    public AccountId {
        this.assertArgumentNotEmpty(value, "'accountId' should not be empty");
    }

}
