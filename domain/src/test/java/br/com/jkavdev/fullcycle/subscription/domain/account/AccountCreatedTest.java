package br.com.jkavdev.fullcycle.subscription.domain.account;

import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;
import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.person.Document;
import br.com.jkavdev.fullcycle.subscription.domain.person.Email;
import br.com.jkavdev.fullcycle.subscription.domain.person.Name;
import br.com.jkavdev.fullcycle.subscription.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class AccountCreatedTest {

    @Test
    public void givenValidParams_whenInstantiate_shouldReturnIt() {
        // given
        final var expectedId = new AccountId("qualquerId");
        final var expectedUserId = new UserId("qualquerUserId");
        final var expectedName = new Name("qualquerNome", "qualquerSobrenome");
        final var expectedEmail = new Email("qualquerEmail@email.com");
        final var expectedDocument = Document.create("qualquerCpf", "cpf");
        final var expectedAggregateType = "Account";

        final var actualAccount = Account.newAccount(
                expectedId,
                expectedUserId,
                expectedEmail,
                expectedName,
                expectedDocument
        );

        // when
        final var actualEvent = new AccountEvent.AccountCreated(actualAccount);

        // then
        Assertions.assertNotNull(actualEvent);
        Assertions.assertEquals(expectedId.value(), actualEvent.aggregateId());
        Assertions.assertEquals(expectedAggregateType, actualEvent.aggregateType());
        Assertions.assertEquals(expectedId.value(), actualEvent.accountId());
        Assertions.assertEquals(expectedEmail.value(), actualEvent.email());
        Assertions.assertEquals(expectedName.fullname(), actualEvent.fullname());
        Assertions.assertNotNull(actualEvent.occurredOn());
    }

    @Test
    public void givenInvalidId_whenInstantiate_shouldReturnError() {
        // given
        final var expectedId = "";
        final var expectedName = "qualquerNome";
        final var expectedEmail = "qualquerEmail@email.com";
        final var expectedOccurredOn = InstantUtils.now();

        final var expectedErrorMessage = "'accountId' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                new AccountEvent.AccountCreated(expectedId, expectedEmail, expectedName, expectedOccurredOn));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidFullname_whenInstantiate_shouldReturnError() {
        // given
        final var expectedId = "qualquerId";
        final var expectedName = "";
        final var expectedEmail = "qualquerEmail@email.com";
        final var expectedOccurredOn = InstantUtils.now();

        final var expectedErrorMessage = "'fullname' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                new AccountEvent.AccountCreated(expectedId, expectedEmail, expectedName, expectedOccurredOn));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidEmail_whenInstantiate_shouldReturnError() {
        // given
        final var expectedId = "qualquerId";
        final var expectedName = "qualquerNome";
        final var expectedEmail = "";
        final var expectedOccurredOn = InstantUtils.now();

        final var expectedErrorMessage = "'email' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                new AccountEvent.AccountCreated(expectedId, expectedEmail, expectedName, expectedOccurredOn));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidOccurredOn_whenInstantiate_shouldReturnError() {
        // given
        final var expectedId = "qualquerId";
        final var expectedName = "qualquerNome";
        final var expectedEmail = "qualquerEmail@email.com";
        final Instant expectedOccurredOn = null;

        final var expectedErrorMessage = "'occurredOn' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                new AccountEvent.AccountCreated(expectedId, expectedEmail, expectedName, expectedOccurredOn));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}
