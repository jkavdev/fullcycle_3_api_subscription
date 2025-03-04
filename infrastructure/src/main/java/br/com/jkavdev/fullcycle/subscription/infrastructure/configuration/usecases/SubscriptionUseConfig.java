package br.com.jkavdev.fullcycle.subscription.infrastructure.configuration.usecases;

import br.com.jkavdev.fullcycle.subscription.application.subscription.CancelSubscription;
import br.com.jkavdev.fullcycle.subscription.application.subscription.ChargeSubscription;
import br.com.jkavdev.fullcycle.subscription.application.subscription.CreateSubscription;
import br.com.jkavdev.fullcycle.subscription.application.subscription.impl.DefaultCancelSubscription;
import br.com.jkavdev.fullcycle.subscription.application.subscription.impl.DefaultChargeSubscription;
import br.com.jkavdev.fullcycle.subscription.application.subscription.impl.DefaultCreateSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.payment.PaymentGateway;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration(proxyBeanMethods = false)
public class SubscriptionUseConfig {

    @Bean
    CreateSubscription createSubscription(
            final AccountGateway accountGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultCreateSubscription(accountGateway, planGateway, subscriptionGateway);
    }

    @Bean
    ChargeSubscription chargeSubscription(
            final AccountGateway accountGateway,
            final Clock clock,
            final PaymentGateway paymentGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultChargeSubscription(accountGateway, clock, paymentGateway, planGateway, subscriptionGateway);
    }

    @Bean
    CancelSubscription cancelSubscription(final SubscriptionGateway subscriptionGateway) {
        return new DefaultCancelSubscription(subscriptionGateway);
    }

}
