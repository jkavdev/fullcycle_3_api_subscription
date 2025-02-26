package br.com.jkavdev.fullcycle.subscription.application.account.impl;

import br.com.jkavdev.fullcycle.subscription.application.account.CreateAccount;
import br.com.jkavdev.fullcycle.subscription.domain.UnitTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;
import br.com.jkavdev.fullcycle.subscription.domain.person.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class DefaultCreateAccountTest extends UnitTest {

    @InjectMocks
    DefaultCreateAccount target;

    @Mock
    AccountGateway accountGateway;

    @Test
    public void givenValidInput_whenCallsExecute_shouldReturnAccountId() {
        // given
        final var expectedFirstname = "qualquerNome";
        final var expectedLastname = "qualquerSobrenome";
        final var expectedEmail = "qualquerEmail@email";
        final var expectedDocumentNumber = "12345678910";
        final var expectedDocumentType = Document.Cpf.TYPE;
        final var expectedUserId = new UserId("qualquerId");
        final var expectedAccountId = new AccountId("qualquerId");

        Mockito.when(accountGateway.nextId())
                .thenReturn(expectedAccountId);
        Mockito.when(accountGateway.save(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var actualOutput =
                target.execute(new CreateAccountTestInput(
                        expectedUserId.value(),
                        expectedFirstname,
                        expectedLastname,
                        expectedEmail,
                        expectedDocumentNumber,
                        expectedDocumentType
                ));

        // then
        Assertions.assertEquals(expectedAccountId, actualOutput.accountId());

    }

    record CreateAccountTestInput(
            String userId,
            String firstname,
            String lastname,
            String email,
            String documentNumber,
            String documentType
    ) implements CreateAccount.Input {

    }

}