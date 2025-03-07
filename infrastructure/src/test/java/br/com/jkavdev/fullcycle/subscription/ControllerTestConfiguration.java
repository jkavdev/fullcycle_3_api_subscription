package br.com.jkavdev.fullcycle.subscription;

import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

public class ControllerTestConfiguration {

    @Bean
    public AccountGateway accountGateway() {
        return Mockito.mock(AccountGateway.class);
    }

}
