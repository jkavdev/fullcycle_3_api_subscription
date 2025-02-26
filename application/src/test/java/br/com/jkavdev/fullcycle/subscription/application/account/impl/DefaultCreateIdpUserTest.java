package br.com.jkavdev.fullcycle.subscription.application.account.impl;

import br.com.jkavdev.fullcycle.subscription.application.account.CreateIdpUser;
import br.com.jkavdev.fullcycle.subscription.domain.UnitTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

class DefaultCreateIdpUserTest extends UnitTest {

    @InjectMocks
    DefaultCreateIdpUser target;

    @Mock
    IdentityProviderGateway identityProviderGateway;

    @Test
    public void givenValidInput_whenCallsExecute_shouldReturnUserId() {
        // given
        final var expectedFirstname = "qualquerNome";
        final var expectedLastname = "qualquerSobrenome";
        final var expectedEmail = "qualquerEmail@email";
        final var expectedPassword = "qualquerSenha";
        final var expectedUserId = new UserId("qualquerId");

        Mockito.when(identityProviderGateway.create(Mockito.any()))
                .thenReturn(expectedUserId);

        // when
        final var actualOutput =
                target.execute(new CreateIdpUserTestInput(
                        expectedFirstname,
                        expectedLastname,
                        expectedEmail,
                        expectedPassword
                ));

        // then
        Assertions.assertEquals(expectedUserId, actualOutput.idpUserId());

    }

    record CreateIdpUserTestInput(
            String firstname,
            String lastname,
            String email,
            String password
    ) implements CreateIdpUser.Input {

    }

}