package br.com.jkavdev.fullcycle.subscription.application.account.impl;

import br.com.jkavdev.fullcycle.subscription.application.account.CreateIdpUser;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.User;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;
import br.com.jkavdev.fullcycle.subscription.domain.person.Email;
import br.com.jkavdev.fullcycle.subscription.domain.person.Name;

import java.util.Objects;

public class DefaultCreateIdpUser extends CreateIdpUser {

    private final IdentityProviderGateway identityProviderGateway;

    public DefaultCreateIdpUser(final IdentityProviderGateway identityProviderGateway) {
        this.identityProviderGateway = Objects.requireNonNull(identityProviderGateway);
    }

    @Override
    public Output execute(final Input input) {
        return new StdOutput(identityProviderGateway.create(userWith(input)));
    }

    private User userWith(final Input input) {
        return User.newUser(
                new Name(input.firstname(), input.lastname()),
                new Email(input.email()),
                input.password()
        );
    }

    record StdOutput(UserId idpUserId) implements Output {

    }

}
