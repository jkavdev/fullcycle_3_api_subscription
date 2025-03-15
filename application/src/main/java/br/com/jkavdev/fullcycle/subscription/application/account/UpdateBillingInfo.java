package br.com.jkavdev.fullcycle.subscription.application.account;

import br.com.jkavdev.fullcycle.subscription.application.UseCase;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;

public abstract class UpdateBillingInfo extends UseCase<UpdateBillingInfo.Input, UpdateBillingInfo.Output> {

    public interface Input {
        String accountId();

        String zipcode();

        String number();

        String complement();

        String country();
    }

    public interface Output {
        AccountId accountId();
    }

}