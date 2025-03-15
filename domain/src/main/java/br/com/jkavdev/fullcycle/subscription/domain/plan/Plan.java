package br.com.jkavdev.fullcycle.subscription.domain.plan;

import br.com.jkavdev.fullcycle.subscription.domain.AggregateRoot;
import br.com.jkavdev.fullcycle.subscription.domain.money.Money;
import br.com.jkavdev.fullcycle.subscription.domain.utils.InstantUtils;

import java.time.Instant;

public class Plan extends AggregateRoot<PlanId> {

    private int version;

    private String name;

    private String description;

    private boolean active;

    private Money price;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant deletedAt;

    public Plan(
            final PlanId planId,
            final int aVersion,
            final String aName,
            final String aDescription,
            final Boolean anActive,
            final Money aPrice,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        super(planId);
        setVersion(aVersion);
        setName(aName);
        setDescription(aDescription);
        setActive(anActive);
        setPrice(aPrice);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    public static Plan newPlan(
            final PlanId planId,
            final String aName,
            final String aDescription,
            final Boolean anActive,
            final Money aPrice
    ) {
        final var now = InstantUtils.now();
        final var isActive = anActive != null ? anActive : false;
        return new Plan(
                planId,
                0,
                aName,
                aDescription,
                isActive,
                aPrice,
                now,
                now,
                isActive ? null : now // se inativo, entao preenche deletado quando
        );
    }

    public void execute(final PlanCommand... cmds) {
        if (cmds == null) {
            return;
        }

        for (final var cmd : cmds) {
            switch (cmd) {
                case PlanCommand.ActivatePlan c -> apply(c);
                case PlanCommand.InactivePlan c -> apply(c);
                case PlanCommand.ChangePlan c -> apply(c);
            }
        }

        setVersion(version() + 1);
        setUpdatedAt(InstantUtils.now());
    }

    public static Plan with(
            final PlanId planId,
            final int aVersion,
            final String aName,
            final String aDescription,
            final Boolean anActive,
            final Money aPrice,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new Plan(
                planId,
                aVersion,
                aName,
                aDescription,
                anActive,
                aPrice,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public Plan withId(final PlanId aPlanId) {
        return Plan.with(
                aPlanId,
                version(),
                name(),
                description(),
                active(),
                price(),
                createdAt(),
                updatedAt(),
                deletedAt()
        );
    }

    public int version() {
        return version;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public boolean active() {
        return active;
    }

    public Money price() {
        return price;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Instant deletedAt() {
        return deletedAt;
    }

    private void apply(final PlanCommand.ActivatePlan cmd) {
        setDeletedAt(null);
        setActive(true);
    }

    private void apply(final PlanCommand.InactivePlan cmd) {
        setDeletedAt(deletedAt() != null ? deletedAt() : InstantUtils.now());
        setActive(false);
    }

    private void apply(final PlanCommand.ChangePlan cmd) {
        setName(cmd.name());
        setDescription(cmd.description());
        setPrice(cmd.money());
        if (Boolean.TRUE.equals(cmd.active())) {
            apply(new PlanCommand.ActivatePlan());
        } else {
            apply(new PlanCommand.InactivePlan());
        }
    }

    private void setVersion(final int version) {
        this.version = version;
    }

    private void setName(final String name) {
        this.assertArgumentNotEmpty(name, "'name' should not be empty");
        this.assertArgumentMaxLength(name, 100, "'name' should have more than 100 characters");
        this.name = name;
    }

    private void setDescription(final String description) {
        this.assertArgumentNotEmpty(description, "'description' should not be empty");
        this.assertArgumentMaxLength(description, 500, "'description' should not have more than 500 characters");
        this.description = description;
    }

    private void setActive(final Boolean active) {
        this.assertArgumentNotNull(active, "'active' should not be null");
        this.active = active;
    }

    private void setPrice(final Money price) {
        this.assertArgumentNotNull(price, "'price' should not be null");
        this.price = price;
    }

    private void setCreatedAt(final Instant createdAt) {
        this.assertArgumentNotNull(createdAt, "'createdAt' should not be null");
        this.createdAt = createdAt;
    }

    private void setUpdatedAt(final Instant updatedAt) {
        this.assertArgumentNotNull(updatedAt, "'updatedAt' should not be null");
        this.updatedAt = updatedAt;
    }

    private void setDeletedAt(final Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
