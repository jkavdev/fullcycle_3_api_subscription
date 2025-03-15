package br.com.jkavdev.fullcycle.subscription.infrastructure.configuration.usecases;

import br.com.jkavdev.fullcycle.subscription.application.account.*;
import br.com.jkavdev.fullcycle.subscription.application.account.impl.*;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AccountUseCaseConfig {

    @Bean
    CreateAccount createAccount(final AccountGateway accountGateway) {
        return new DefaultCreateAccount(accountGateway);
    }

    @Bean
    CreateIdpUser createIdpUser(final IdentityProviderGateway identityProviderGateway) {
        return new DefaultCreateIdpUser(identityProviderGateway);
    }

    @Bean
    AddToGroup addToGroup(
            final IdentityProviderGateway identityProviderGateway,
            final SubscriptionGateway subscriptionGateway,
            final AccountGateway accountGateway
    ) {
        return new DefaultAddToGroup(identityProviderGateway, subscriptionGateway, accountGateway);
    }

    @Bean
    RemoveFromGroup removeFromGroup(
            final IdentityProviderGateway identityProviderGateway,
            final SubscriptionGateway subscriptionGateway,
            final AccountGateway accountGateway
    ) {
        return new DefaultRemoveFromGroup(identityProviderGateway, subscriptionGateway, accountGateway);
    }

    @Bean
    UpdateBillingInfo updateBillingInfo(final AccountGateway accountGateway) {
        return new DefaultUpdateBillingInfo(accountGateway);
    }

}
