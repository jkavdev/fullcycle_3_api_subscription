package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.domain.DomainEvent;
import br.com.jkavdev.fullcycle.subscription.domain.utils.InstantUtils;
import br.com.jkavdev.fullcycle.subscription.infrastructure.jdbc.DatabaseClient;
import br.com.jkavdev.fullcycle.subscription.infrastructure.jdbc.RowMap;
import br.com.jkavdev.fullcycle.subscription.infrastructure.json.Json;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

@Repository
public class EventJdbcRepository {

    private final DatabaseClient database;

    public EventJdbcRepository(final DatabaseClient database) {
        this.database = Objects.requireNonNull(database);
    }

    public List<DomainEvent> allEventsOfAggregate(final String aggregateId, final String aggregateType) {
        final var sql = """
                SELECT
                    event_id,
                    processed,
                    aggregate_id,
                    aggregate_type,
                    event_type,
                    event_date,
                    event_data
                FROM events
                WHERE aggregate_id = :aggregateId AND aggregate_type = :aggregateType
                """;
        final var params = Map.<String, Object>of(
                "aggregateId", aggregateId,
                "aggregateType", aggregateType
        );
        return database.query(
                        sql,
                        params,
                        eventMapper()
                )
                .stream()
                .map(this::toDomainEvent)
                .toList();
    }

    public void saveAll(final Collection<DomainEvent> events) {
        for (final var ev : events) {
            insertEvent(Event.newEvent(
                    ev.aggregateId(),
                    ev.aggregateType(),
                    // para inner classes usamos o type name, pois incluira o $ que eh necessario ao deserializar a classe
                    ev.getClass().getTypeName(),
                    Json.writeValueAsBytes(ev)
            ));
        }
    }

    private void insertEvent(final Event event) {
        final var sql = """
                INSERT INTO events (
                    processed,
                    aggregate_id,
                    aggregate_type,
                    event_type,
                    event_date,
                    event_data
                )
                VALUES (
                    :processed,
                    :aggregateId,
                    :aggregateType,
                    :eventType,
                    :eventDate,
                    :eventData
                )
                """;

        final var params = new HashMap<String, Object>();
        params.put("processed", event.processed());
        params.put("aggregateId", event.aggregateId());
        params.put("aggregateType", event.aggregateType());
        params.put("eventType", event.eventType());
        params.put("eventDate", event.eventDate());
        params.put("eventData", event.eventData());

        database.update(sql, params);

    }

    private DomainEvent toDomainEvent(final Event event) {
        try {
            return (DomainEvent) Json.readValue(event.eventData(), Class.forName(event.eventType()));
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    private RowMap<Event> eventMapper() {
        return (rs) -> new Event(
                rs.getLong("event_id"),
                rs.getBoolean("processed"),
                rs.getString("aggregate_id"),
                rs.getString("aggregate_type"),
                rs.getString("event_type"),
                rs.getObject("event_date", Instant.class),
                rs.getBytes("event_data")
        );
    }

    private record Event(
            Long eventId,
            boolean processed,
            String aggregateId,
            String aggregateType,
            String eventType,
            Instant eventDate,
            byte[] eventData
    ) {

        public static Event newEvent(
                final String aggregateId,
                final String aggregateType,
                final String eventType,
                final byte[] eventData
        ) {
            return new Event(
                    null,
                    false,
                    aggregateId,
                    aggregateType,
                    eventType,
                    InstantUtils.now(),
                    eventData
            );
        }

    }

}
