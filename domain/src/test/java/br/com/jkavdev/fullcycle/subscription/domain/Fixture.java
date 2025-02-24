package br.com.jkavdev.fullcycle.subscription.domain;

import br.com.jkavdev.fullcycle.subscription.domain.money.Money;
import br.com.jkavdev.fullcycle.subscription.domain.plan.Plan;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;
import net.datafaker.Faker;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static final class Plans {

        public static Plan plus() {
            return Plan.newPlan(
                    new PlanId(123456L),
                    "plus",
                    FAKER.text().text(100, 1000),
                    true,
                    new Money("BRL", 100.00)
            );
        }

    }

}
