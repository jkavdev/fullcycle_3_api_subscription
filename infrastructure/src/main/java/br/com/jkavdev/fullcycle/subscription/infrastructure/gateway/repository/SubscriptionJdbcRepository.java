package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
import br.com.jkavdev.fullcycle.subscription.infrastructure.jdbc.DatabaseClient;
import br.com.jkavdev.fullcycle.subscription.infrastructure.jdbc.RowMap;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class SubscriptionJdbcRepository implements SubscriptionGateway {

    private final DatabaseClient database;

    private final EventJdbcRepository eventRepository;

    public SubscriptionJdbcRepository(
            final DatabaseClient database,
            final EventJdbcRepository eventRepository
    ) {
        this.database = Objects.requireNonNull(database);
        this.eventRepository = Objects.requireNonNull(eventRepository);
    }


    @Override
    public SubscriptionId nextId() {
        return null;
    }

    @Override
    public Optional<Subscription> subscriptionOfId(final SubscriptionId anId) {
        final var sql = """
                SELECT
                    id,
                    version,
                    account_id,
                    plan_id,
                    created_at,
                    updated_at,
                    due_date,
                    status,
                    last_renew_dt,
                    last_transaction_id
                FROM subscriptions
                WHERE id = :id
                """;
        return database.queryOne(
                sql,
                Map.of("id", anId.value()),
                subscriptionMapper()
        );
    }

    @Override
    public Subscription save(Subscription subscription) {
        return null;
    }

    @Override
    public Optional<Subscription> latestSubscriptionOfAccount(AccountId accountId) {
        return Optional.empty();
    }

    private RowMap<Subscription> subscriptionMapper() {
        return (rs) -> Subscription.with(
                new SubscriptionId(rs.getString("id")),
                rs.getInt("version"),
                new AccountId(rs.getString("account_id")),
                new PlanId(rs.getLong("plan_id")),
                rs.getDate("due_date").toLocalDate(),
                rs.getString("status"),
                rs.getObject("last_renew_dt", Instant.class),
                rs.getString("last_transaction_id"),
                rs.getObject("created_at", Instant.class),
                rs.getObject("updated_at", Instant.class)
        );
    }

}
