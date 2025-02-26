package br.com.jkavdev.fullcycle.subscription.application.account;

import br.com.jkavdev.fullcycle.subscription.application.UseCase;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;

public abstract class CreateIdpUser extends UseCase<CreateIdpUser.Input, CreateIdpUser.Output> {

    public interface Input {
        String firstname();
        String lastname();
        String email();
        String password();
    }

    public interface Output{
        UserId idpUserId();
    }

}
