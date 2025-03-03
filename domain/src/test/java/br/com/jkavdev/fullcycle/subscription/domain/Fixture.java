package br.com.jkavdev.fullcycle.subscription.domain;

import br.com.jkavdev.fullcycle.subscription.domain.account.Account;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;
import br.com.jkavdev.fullcycle.subscription.domain.money.Money;
import br.com.jkavdev.fullcycle.subscription.domain.person.Document;
import br.com.jkavdev.fullcycle.subscription.domain.person.Email;
import br.com.jkavdev.fullcycle.subscription.domain.person.Name;
import br.com.jkavdev.fullcycle.subscription.domain.plan.Plan;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.Subscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
import net.datafaker.Faker;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static final class Accounts {

        public static Account jhou() {
            return Account.newAccount(
                    new AccountId("acc-jhou"),
                    new UserId("user-jhou"),
                    new Email("jhou@email"),
                    new Name("Jhou", "Fucker"),
                    Document.create("12345678910", Document.Cpf.TYPE)
            );
        }

        public static Account malu() {
            return Account.newAccount(
                    new AccountId("acc-malu"),
                    new UserId("user-malu"),
                    new Email("malu@email"),
                    new Name("Malu", "Fucker"),
                    Document.create("12345678924", Document.Cpf.TYPE)
            );
        }

    }

    public static final class Plans {

        public static Plan plus() {
            return Plan.newPlan(
                    new PlanId(123456L),
                    "plus",
                    FAKER.text().text(100, 500),
                    true,
                    new Money("BRL", 100.00)
            );
        }

    }

    public static final class Subscriptions {

        public static Subscription jhous() {
            return Subscription.newSubscription(
                    new SubscriptionId("sub-jhou"),
                    Accounts.jhou().id(),
                    Plans.plus()
            );
        }

    }

}
