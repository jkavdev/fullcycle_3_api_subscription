package br.com.jkavdev.fullcycle.subscription.domain;

import java.util.Collections;
import java.util.List;

/**
 * objeto que faz a fronteira dos value objects com os repositorios
 */

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AggregateRoot(final ID id) {
        this(id, Collections.emptyList());
    }

    protected AggregateRoot(final ID id, final List<DomainEvent> events) {
        super(id, events);
    }
}
