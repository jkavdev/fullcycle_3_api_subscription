package br.com.jkavdev.fullcycle.subscription.domain;

import java.time.Instant;

public interface DomainEvent extends AssertionConcern {

    Instant occurredOn();

    String aggregateId();

    String aggregateType();

}
