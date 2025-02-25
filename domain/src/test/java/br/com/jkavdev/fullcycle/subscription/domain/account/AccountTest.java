package br.com.jkavdev.fullcycle.subscription.domain.account;

import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.person.Address;
import br.com.jkavdev.fullcycle.subscription.domain.person.Document;
import br.com.jkavdev.fullcycle.subscription.domain.person.Email;
import br.com.jkavdev.fullcycle.subscription.domain.person.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 1 - caminho feliz de um novo agregado
 * 2 - caminho feliz restauracao do agregado
 * 3 - caminho de validacao
 */
public class AccountTest {

    @Test
    public void givenValidParams_whenCallsNewAccount_shouldInstantiateAndDispatchEvent() {
        // given
        final var expectedId = new AccountId("qualquerId");
        final var expectedVersion = 0;
        final var expectedUserId = new UserId("qualquerUserId");
        final var expectedName = new Name("qualquerNome", "qualquerSobrenome");
        final var expectedEmail = new Email("qualquerEmail@email.com");
        final var expectedDocument = Document.create("qualquerCpf", "cpf");
        final var expectedEvents = 1;

        // when
        final var actualAccount = Account.newAccount(
                expectedId,
                expectedUserId,
                expectedEmail,
                expectedName,
                expectedDocument
        );

        // then
        Assertions.assertNotNull(actualAccount);
        Assertions.assertEquals(expectedId, actualAccount.id());
        Assertions.assertEquals(expectedVersion, actualAccount.version());
        Assertions.assertEquals(expectedUserId, actualAccount.userId());
        Assertions.assertEquals(expectedEmail, actualAccount.email());
        Assertions.assertEquals(expectedName, actualAccount.name());
        Assertions.assertEquals(expectedDocument, actualAccount.document());
        Assertions.assertNull(actualAccount.billingAddress());

        Assertions.assertEquals(expectedEvents, actualAccount.domainEvents().size());
        Assertions.assertInstanceOf(AccountEvent.AccountCreated.class, actualAccount.domainEvents().getFirst());
    }

    @Test
    public void givenValidParams_whenCallsWith_shouldInstantiate() {
        // given
        final var expectedId = new AccountId("qualquerId");
        final var expectedVersion = 1;
        final var expectedUserId = new UserId("qualquerUserId");
        final var expectedName = new Name("qualquerNome", "qualquerSobrenome");
        final var expectedEmail = new Email("qualquerEmail@email.com");
        final var expectedDocument = Document.create("qualquerCpf", "cpf");
        final var expectedAddress = new Address("qualquerCep", "qualquerNumero", "qualquerLugar", "qualquerPais");

        // when
        final var actualAccount = Account.with(
                expectedId,
                expectedVersion,
                expectedUserId,
                expectedEmail,
                expectedName,
                expectedDocument,
                expectedAddress
        );

        // then
        Assertions.assertNotNull(actualAccount);
        Assertions.assertEquals(expectedId, actualAccount.id());
        Assertions.assertEquals(expectedVersion, actualAccount.version());
        Assertions.assertEquals(expectedUserId, actualAccount.userId());
        Assertions.assertEquals(expectedEmail, actualAccount.email());
        Assertions.assertEquals(expectedName, actualAccount.name());
        Assertions.assertEquals(expectedDocument, actualAccount.document());
        Assertions.assertEquals(expectedAddress, actualAccount.billingAddress());
    }

    @Test
    public void givenInvalidId_whenCallsWith_shouldReturnError() {
        // given
        final AccountId expectedId = null;
        final var expectedVersion = 1;
        final var expectedUserId = new UserId("qualquerUserId");
        final var expectedName = new Name("qualquerNome", "qualquerSobrenome");
        final var expectedEmail = new Email("qualquerEmail@email.com");
        final var expectedDocument = Document.create("qualquerCpf", "cpf");
        final var expectedAddress = new Address("qualquerCep", "qualquerNumero", "qualquerLugar", "qualquerPais");

        final var expectedErrorMessage = "'id' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Account.with(
                        expectedId,
                        expectedVersion,
                        expectedUserId,
                        expectedEmail,
                        expectedName,
                        expectedDocument,
                        expectedAddress
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidUserId_whenCallsWith_shouldReturnError() {
        // given
        final var expectedId = new AccountId("qualquerId");
        final var expectedVersion = 0;
        final UserId expectedUserId = null;
        final var expectedName = new Name("qualquerNome", "qualquerSobrenome");
        final var expectedEmail = new Email("qualquerEmail@email.com");
        final var expectedDocument = Document.create("qualquerCpf", "cpf");
        final var expectedAddress = new Address("qualquerCep", "qualquerNumero", "qualquerLugar", "qualquerPais");

        final var expectedErrorMessage = "'userId' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Account.with(
                        expectedId,
                        expectedVersion,
                        expectedUserId,
                        expectedEmail,
                        expectedName,
                        expectedDocument,
                        expectedAddress
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidName_whenCallsWith_shouldReturnError() {
        // given
        final var expectedId = new AccountId("qualquerId");
        final var expectedVersion = 1;
        final var expectedUserId = new UserId("qualquerUserId");
        final Name expectedName = null;
        final var expectedEmail = new Email("qualquerEmail@email.com");
        final var expectedDocument = Document.create("qualquerCpf", "cpf");
        final var expectedAddress = new Address("qualquerCep", "qualquerNumero", "qualquerLugar", "qualquerPais");

        final var expectedErrorMessage = "'name' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Account.with(
                        expectedId,
                        expectedVersion,
                        expectedUserId,
                        expectedEmail,
                        expectedName,
                        expectedDocument,
                        expectedAddress
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidEmail_whenCallsWith_shouldReturnError() {
        // given
        final var expectedId = new AccountId("qualquerId");
        final var expectedVersion = 1;
        final var expectedUserId = new UserId("qualquerUserId");
        final var expectedName = new Name("qualquerNome", "qualquerSobrenome");
        final Email expectedEmail = null;
        final var expectedDocument = Document.create("qualquerCpf", "cpf");
        final var expectedAddress = new Address("qualquerCep", "qualquerNumero", "qualquerLugar", "qualquerPais");

        final var expectedErrorMessage = "'email' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Account.with(
                        expectedId,
                        expectedVersion,
                        expectedUserId,
                        expectedEmail,
                        expectedName,
                        expectedDocument,
                        expectedAddress
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidDocument_whenCallsWith_shouldReturnError() {
        // given
        final var expectedId = new AccountId("qualquerId");
        final var expectedVersion = 1;
        final var expectedUserId = new UserId("qualquerUserId");
        final var expectedName = new Name("qualquerNome", "qualquerSobrenome");
        final var expectedEmail = new Email("qualquerEmail@email.com");
        final Document expectedDocument = null;
        final var expectedAddress = new Address("qualquerCep", "qualquerNumero", "qualquerLugar", "qualquerPais");

        final var expectedErrorMessage = "'document' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Account.with(
                        expectedId,
                        expectedVersion,
                        expectedUserId,
                        expectedEmail,
                        expectedName,
                        expectedDocument,
                        expectedAddress
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullAddress_whenCallsWith_ShouldReturnOK() {
        // given
        final var expectedId = new AccountId("qualquerId");
        final var expectedVersion = 1;
        final var expectedUserId = new UserId("qualquerUserId");
        final var expectedName = new Name("qualquerNome", "qualquerSobrenome");
        final var expectedEmail = new Email("qualquerEmail@email.com");
        final var expectedDocument = Document.create("qualquerCpf", "cpf");
        final Address expectedAddress = null;

        // when
        // then
        Assertions.assertDoesNotThrow(() ->
                Account.with(
                        expectedId,
                        expectedVersion,
                        expectedUserId,
                        expectedEmail,
                        expectedName,
                        expectedDocument,
                        expectedAddress
                ));

    }

    @Test
    public void givenAccount_whenCallsExecuteWithProfileCommand_shouldUpdateNameAndAddress() {
        // given
        final var expectedId = new AccountId("qualquerId");
        final var expectedVersion = 0;
        final var expectedUserId = new UserId("qualquerUserId");
        final var expectedName = new Name("qualquerNome", "qualquerSobrenome");
        final var expectedEmail = new Email("qualquerEmail@email.com");
        final var expectedDocument = Document.create("qualquerCpf", "cpf");
        final var expectedAddress = new Address("qualquerCep", "qualquerNumero", "qualquerLugar", "qualquerPais");
        final var expectedEvents = 0;

        final var actualAccount = Account.with(
                expectedId,
                expectedVersion,
                expectedUserId,
                expectedEmail,
                new Name("qualquerOutroNome", "qualquerOutroSobrenome"),
                expectedDocument,
                null
        );

        // when
        actualAccount.execute(new AccountCommand.ChangeProfileCommand(expectedName, expectedAddress));

        // then
        Assertions.assertNotNull(actualAccount);
        Assertions.assertEquals(expectedId, actualAccount.id());
        Assertions.assertEquals(expectedVersion, actualAccount.version());
        Assertions.assertEquals(expectedUserId, actualAccount.userId());
        Assertions.assertEquals(expectedEmail, actualAccount.email());
        Assertions.assertEquals(expectedName, actualAccount.name());
        Assertions.assertEquals(expectedDocument, actualAccount.document());
        Assertions.assertEquals(expectedAddress, actualAccount.billingAddress());

        Assertions.assertEquals(expectedEvents, actualAccount.domainEvents().size());
    }

    @Test
    public void givenAccount_whenCallsExecuteWithEmailCommand_shouldUpdateEmail() {
        // given
        final var expectedId = new AccountId("qualquerId");
        final var expectedVersion = 0;
        final var expectedUserId = new UserId("qualquerUserId");
        final var expectedName = new Name("qualquerNome", "qualquerSobrenome");
        final var expectedEmail = new Email("qualquerEmail@email.com");
        final var expectedDocument = Document.create("qualquerCpf", "cpf");
        final var expectedAddress = new Address("qualquerCep", "qualquerNumero", "qualquerLugar", "qualquerPais");
        final var expectedEvents = 0;

        final var actualAccount = Account.with(
                expectedId,
                expectedVersion,
                expectedUserId,
                new Email("qualquerOutroEmail@email.com"),
                expectedName,
                expectedDocument,
                expectedAddress
        );

        // when
        actualAccount.execute(new AccountCommand.ChangeEmailCommand(expectedEmail));

        // then
        Assertions.assertNotNull(actualAccount);
        Assertions.assertEquals(expectedId, actualAccount.id());
        Assertions.assertEquals(expectedVersion, actualAccount.version());
        Assertions.assertEquals(expectedUserId, actualAccount.userId());
        Assertions.assertEquals(expectedEmail, actualAccount.email());
        Assertions.assertEquals(expectedName, actualAccount.name());
        Assertions.assertEquals(expectedDocument, actualAccount.document());
        Assertions.assertEquals(expectedAddress, actualAccount.billingAddress());

        Assertions.assertEquals(expectedEvents, actualAccount.domainEvents().size());
    }

    @Test
    public void givenAccount_whenCallsExecuteWithDocumentCommand_shouldUpdateDocument() {
        // given
        final var expectedId = new AccountId("qualquerId");
        final var expectedVersion = 0;
        final var expectedUserId = new UserId("qualquerUserId");
        final var expectedName = new Name("qualquerNome", "qualquerSobrenome");
        final var expectedEmail = new Email("qualquerEmail@email.com");
        final var expectedDocument = Document.create("qualquerCpf", "cpf");
        final var expectedAddress = new Address("qualquerCep", "qualquerNumero", "qualquerLugar", "qualquerPais");
        final var expectedEvents = 0;

        final var actualAccount = Account.with(
                expectedId,
                expectedVersion,
                expectedUserId,
                expectedEmail,
                expectedName,
                Document.create("12345678910111", "cnpj"),
                expectedAddress
        );

        // when
        actualAccount.execute(new AccountCommand.ChangeDocumentCommand(expectedDocument));

        // then
        Assertions.assertNotNull(actualAccount);
        Assertions.assertEquals(expectedId, actualAccount.id());
        Assertions.assertEquals(expectedVersion, actualAccount.version());
        Assertions.assertEquals(expectedUserId, actualAccount.userId());
        Assertions.assertEquals(expectedEmail, actualAccount.email());
        Assertions.assertEquals(expectedName, actualAccount.name());
        Assertions.assertEquals(expectedDocument, actualAccount.document());
        Assertions.assertEquals(expectedAddress, actualAccount.billingAddress());

        Assertions.assertEquals(expectedEvents, actualAccount.domainEvents().size());
    }

}
