package br.com.jkavdev.fullcycle.subscription.infrastructure.configuration.usecases;

import br.com.jkavdev.fullcycle.subscription.application.plan.ChangePlan;
import br.com.jkavdev.fullcycle.subscription.application.plan.CreatePlan;
import br.com.jkavdev.fullcycle.subscription.application.plan.impl.DefaultChangePlan;
import br.com.jkavdev.fullcycle.subscription.application.plan.impl.DefaultCreatePlan;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class PlanUseConfig {

    @Bean
    CreatePlan createPlan(final PlanGateway planGateway) {
        return new DefaultCreatePlan(planGateway);
    }

    @Bean
    ChangePlan changePlan(final PlanGateway planGateway) {
        return new DefaultChangePlan(planGateway);
    }

}
