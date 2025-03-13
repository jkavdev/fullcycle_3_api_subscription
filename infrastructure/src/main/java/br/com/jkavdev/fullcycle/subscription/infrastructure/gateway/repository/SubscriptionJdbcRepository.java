package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
import br.com.jkavdev.fullcycle.subscription.domain.utils.IdUtils;
import br.com.jkavdev.fullcycle.subscription.infrastructure.jdbc.DatabaseClient;
import br.com.jkavdev.fullcycle.subscription.infrastructure.jdbc.RowMap;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
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
        return new SubscriptionId(IdUtils.uniqueId());
    }

    @Override
    public Optional<Subscription> latestSubscriptionOfAccount(final AccountId accountId) {
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
                WHERE account_id = :accountId
                """;
        return database.queryOne(
                sql,
                Map.of("accountId", accountId.value()),
                subscriptionMapper()
        );
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
    @Transactional
    public Subscription save(final Subscription subscription) {
        if (subscription.version() == 0) {
            create(subscription);
        } else {
            update(subscription);
        }

        eventRepository.saveAll(subscription.domainEvents());

        return subscription;
    }

    private void create(final Subscription subscription) {
        final var sql = """
                INSERT INTO subscriptions(
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
                )
                values(
                    :id,
                    (:version + 1),
                    :accountId,
                    :planId,
                    :createdAt,
                    :updatedAt,
                    :dueDate,
                    :status,
                    :lastRenewDt,
                    :lastTransactionId
                )
                """;
        executeUpdate(sql, subscription);
    }

    private void update(final Subscription subscription) {
        final var sql = """
                UPDATE subscriptions SET
                    version = :version + 1,
                    account_id = :accountId,
                    plan_id = :planId,
                    created_at = :createdAt,
                    updated_at = :updatedAt,
                    due_date = :dueDate,
                    status = :status,
                    last_renew_dt = :lastRenewDt,
                    last_transaction_id = :lastTransactionId
                WHERE id = :id AND version = :version
                """;

        if (executeUpdate(sql, subscription) == 0) {
            throw new IllegalArgumentException(
                    "subscription with id %s and version %s was not fount".formatted(
                            subscription.id().value(), subscription.version()
                    )
            );
        }

    }

    private int executeUpdate(final String sql, final Subscription subscription) {
        final var params = new HashMap<String, Object>();
        params.put("id", subscription.id().value());
        params.put("version", subscription.version());
        params.put("accountId", subscription.accountId().value());
        params.put("planId", subscription.planId().value());
        params.put("createdAt", subscription.createdAt());
        params.put("updatedAt", subscription.updatedAt());
        params.put("dueDate", subscription.dueDate());
        params.put("status", subscription.status().value());
        params.put("lastRenewDt", subscription.lastRenewDate());
        params.put("lastTransactionId", subscription.lastTransactionId());


        return database.update(sql, params);
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
