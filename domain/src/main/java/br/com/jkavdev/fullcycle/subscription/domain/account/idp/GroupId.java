package br.com.jkavdev.fullcycle.subscription.domain.account.idp;

import br.com.jkavdev.fullcycle.subscription.domain.Identifier;

public record GroupId(String value) implements Identifier<String> {

    public GroupId {
        this.assertArgumentNotEmpty(value, "'userId' should not be empty");
    }

}
