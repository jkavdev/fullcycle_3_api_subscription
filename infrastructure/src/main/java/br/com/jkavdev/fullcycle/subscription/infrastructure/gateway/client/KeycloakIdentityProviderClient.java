package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.client;

import br.com.jkavdev.fullcycle.subscription.domain.account.idp.GroupId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.User;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;
import br.com.jkavdev.fullcycle.subscription.domain.utils.IdUtils;
import org.springframework.stereotype.Component;

@Component
public class KeycloakIdentityProviderClient implements IdentityProviderGateway {

    @Override
    public UserId create(User anUser) {
        return new UserId(IdUtils.uniqueId());
    }

    @Override
    public void addUserToGroup(UserId anId, GroupId aGroupId) {

    }

    @Override
    public void removeUserFromGroup(UserId anId, GroupId aGroupId) {

    }
}
