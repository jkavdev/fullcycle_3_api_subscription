package br.com.jkavdev.fullcycle.subscription.domain.account.iam;

import br.com.jkavdev.fullcycle.subscription.domain.Identifier;

public record UserId(String value) implements Identifier<String> {

    public UserId {
        this.assertArgumentNotEmpty(value, "'userId' should not be empty");
    }

}
