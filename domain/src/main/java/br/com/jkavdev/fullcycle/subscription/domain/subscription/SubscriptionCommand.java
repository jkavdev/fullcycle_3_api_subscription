package br.com.jkavdev.fullcycle.subscription.domain.subscription;

import br.com.jkavdev.fullcycle.subscription.domain.AssertionConcern;
import br.com.jkavdev.fullcycle.subscription.domain.plan.Plan;

public sealed interface SubscriptionCommand extends AssertionConcern {

    record IncompleteSubscription(String aReason, String aTransactionId) implements SubscriptionCommand {
        public IncompleteSubscription {
            this.assertArgumentNotEmpty(aTransactionId, "'aTransactionId' should not be empty");
        }
    }

    record RenewSubscription(Plan selectedPlan, String aTransactionId) implements SubscriptionCommand {
        public RenewSubscription {
            this.assertArgumentNotNull(selectedPlan, "'selectedPlan' should not be null");
            this.assertArgumentNotEmpty(aTransactionId, "'aTransactionId' should not be empty");
        }
    }

    record CancelSubscription() implements SubscriptionCommand {
    }

    record ChangeStatus(String status) implements SubscriptionCommand {
        public ChangeStatus {
            this.assertArgumentNotEmpty(status, "'status' should not be empty");
        }
    }

}
