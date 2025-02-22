package br.com.jkavdev.fullcycle.subscription.domain.account.iam;

import br.com.jkavdev.fullcycle.subscription.domain.Identifier;

public record UserId(String value) implements Identifier {

    public UserId {
        this.assertArgumentNotNull(value, "'userId' should not be null");
    }

}
