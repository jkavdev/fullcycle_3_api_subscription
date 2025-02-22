package br.com.jkavdev.fullcycle.subscription.domain.event;

import br.com.jkavdev.fullcycle.subscription.domain.DomainEvent;

@FunctionalInterface
public interface DomainEventPublisher {

    void publishEvent(DomainEvent event);
}
