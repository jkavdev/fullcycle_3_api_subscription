package br.com.jkavdev.fullcycle.subscription.domain.subscription;

import br.com.jkavdev.fullcycle.subscription.domain.AggregateRoot;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.plan.Plan;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionCommand.ChangeStatus;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.status.SubscriptionStatus;
import br.com.jkavdev.fullcycle.subscription.domain.utils.InstantUtils;

import java.time.Instant;
import java.time.LocalDate;

public class Subscription extends AggregateRoot<SubscriptionId> {

    private int version;

    private AccountId accountId;

    private PlanId planId;

    private LocalDate dueDate;

    private SubscriptionStatus status;

    private Instant lastRenewDate;

    private String lastTransactionId;

    private Instant createdAt;

    private Instant updatedAt;

    private Subscription(
            final SubscriptionId subscriptionId,
            final int version,
            final AccountId accountId,
            final PlanId planId,
            final LocalDate dueDate,
            final String status,
            final Instant lastRenewDate,
            final String lastTransactionId,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        super(subscriptionId);

        setVersion(version);
        setAccountId(accountId);
        setPlanId(planId);
        setDueDate(dueDate);
        setStatus(SubscriptionStatus.create(status, this));
        setLastRenewDate(lastRenewDate);
        setLastTransactionId(lastTransactionId);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    public static Subscription newSubscription(
            final SubscriptionId anId,
            final AccountId anAccountId,
            final Plan selectedPlan
    ) {
        final var now = InstantUtils.now();
        return new Subscription(
                anId,
                0,
                anAccountId,
                selectedPlan.id(),
                LocalDate.now().plusMonths(1),
                SubscriptionStatus.TRAILING,
                null,
                null,
                now,
                now
        );
    }

    public static Subscription with(
            final SubscriptionId subscriptionId,
            final int version,
            final AccountId accountId,
            final PlanId planId,
            final LocalDate dueDate,
            final String status,
            final Instant lastRenewDate,
            final String lastTransactionId,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        return new Subscription(
                subscriptionId,
                version,
                accountId,
                planId,
                dueDate,
                status,
                lastRenewDate,
                lastTransactionId,
                createdAt,
                updatedAt
        );
    }

    public void execute(final SubscriptionCommand... cmds) {
        if (cmds == null) {
            return;
        }

        for (final var cmd : cmds) {
            switch (cmd) {
                case ChangeStatus c -> apply(c);
            }
        }

        setUpdatedAt(InstantUtils.now());
    }

    public int version() {
        return version;
    }

    public AccountId accountId() {
        return accountId;
    }

    public PlanId planId() {
        return planId;
    }

    public LocalDate dueDate() {
        return dueDate;
    }

    public SubscriptionStatus status() {
        return status;
    }

    public Instant lastRenewDate() {
        return lastRenewDate;
    }

    public String lastTransactionId() {
        return lastTransactionId;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private void apply(final ChangeStatus cmd) {
        setStatus(cmd.status());
    }

    private void setVersion(int version) {
        this.version = version;
    }

    private void setAccountId(AccountId accountId) {
        this.assertArgumentNotNull(accountId, "'accountId' should not be null");
        this.accountId = accountId;
    }

    private void setPlanId(PlanId planId) {
        this.assertArgumentNotNull(planId, "'planId' should not be null");
        this.planId = planId;
    }

    private void setDueDate(LocalDate dueDate) {
        this.assertArgumentNotNull(dueDate, "'dueDate' should not be null");
        this.dueDate = dueDate;
    }

    private void setStatus(SubscriptionStatus status) {
        this.assertArgumentNotNull(status, "'status' should not be null");
        this.status = status;
    }

    private void setLastRenewDate(Instant lastRenewDate) {
        this.lastRenewDate = lastRenewDate;
    }

    private void setLastTransactionId(String lastTransactionId) {
        this.lastTransactionId = lastTransactionId;
    }

    private void setCreatedAt(Instant createdAt) {
        this.assertArgumentNotNull(createdAt, "'createdAt' should not be null");
        this.createdAt = createdAt;
    }

    private void setUpdatedAt(Instant updatedAt) {
        this.assertArgumentNotNull(updatedAt, "'updatedAt' should not be null");
        this.updatedAt = updatedAt;
    }
}
