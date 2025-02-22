package br.com.jkavdev.fullcycle.subscription.domain.person;

import br.com.jkavdev.fullcycle.subscription.domain.ValueObject;

public record Name(String firstname, String lastname) implements ValueObject {

    public Name {
        this.assertArgumentNotEmpty(firstname, "'firstname' should not be empty");
        this.assertArgumentNotEmpty(lastname, "'lastname' should not be empty");
    }

}
