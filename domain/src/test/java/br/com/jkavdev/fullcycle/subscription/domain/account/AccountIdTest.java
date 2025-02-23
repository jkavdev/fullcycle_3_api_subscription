package br.com.jkavdev.fullcycle.subscription.domain.account;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AccountIdTest {

    @Test
    public void givenValidAccountId_whenInstantiate_shouldReturnValueObject() {
        // given
        final var expectedAccountId = "qualquerUserId";

        // when
        final var actualAccountId = new AccountId(expectedAccountId);

        // then
        Assertions.assertEquals(expectedAccountId, actualAccountId.value());
    }

    @Test
    public void givenEmptyAccountId_whenInstantiate_shouldReturnError() {
        // given
        final var expectedAccountId = " ";

        final var expectedErrorMessage = "'accountId' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new AccountId(expectedAccountId));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}