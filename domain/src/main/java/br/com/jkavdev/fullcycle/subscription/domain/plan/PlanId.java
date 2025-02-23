package br.com.jkavdev.fullcycle.subscription.domain.plan;

import br.com.jkavdev.fullcycle.subscription.domain.Identifier;

public record PlanId(String value) implements Identifier {

    public PlanId {
        this.assertArgumentNotEmpty(value, "'planId' should not be empty");
    }

}
