package br.com.jkavdev.fullcycle.subscription.application.account;

import br.com.jkavdev.fullcycle.subscription.application.UseCase;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;

public abstract class CreateAccount extends UseCase<CreateAccount.Input, CreateAccount.Output> {

    public interface Input {
        String userId();

        String accountId();

        String firstname();

        String lastname();

        String email();

        String documentNumber();

        String documentType();
    }

    public interface Output {
        AccountId accountId();
    }

}
