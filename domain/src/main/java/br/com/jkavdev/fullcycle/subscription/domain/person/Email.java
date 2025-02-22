package br.com.jkavdev.fullcycle.subscription.domain.person;

import br.com.jkavdev.fullcycle.subscription.domain.ValueObject;

public record Email(String value) implements ValueObject {

    public Email {
        this.assertArgumentNotEmpty(value, "'email' should not be empty");
    }

}
