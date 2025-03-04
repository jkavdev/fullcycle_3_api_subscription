package br.com.jkavdev.fullcycle.subscription.infrastructure.configuration.usecases;

import br.com.jkavdev.fullcycle.subscription.application.account.AddToGroup;
import br.com.jkavdev.fullcycle.subscription.application.account.CreateAccount;
import br.com.jkavdev.fullcycle.subscription.application.account.CreateIdpUser;
import br.com.jkavdev.fullcycle.subscription.application.account.RemoveFromGroup;
import br.com.jkavdev.fullcycle.subscription.application.account.impl.DefaultAddToGroup;
import br.com.jkavdev.fullcycle.subscription.application.account.impl.DefaultCreateAccount;
import br.com.jkavdev.fullcycle.subscription.application.account.impl.DefaultCreateIdpUser;
import br.com.jkavdev.fullcycle.subscription.application.account.impl.DefaultRemoveFromGroup;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AccountUseConfig {

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

}
