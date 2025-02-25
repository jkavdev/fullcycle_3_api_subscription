package br.com.jkavdev.fullcycle.subscription.domain.account.idp;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserIdTest {

    @Test
    public void givenValidUserId_whenInstantiate_shouldReturnValueObject() {
        // given
        final var expectedUserId = "qualquerUserId";

        // when
        final var actualUserId = new UserId(expectedUserId);

        // then
        Assertions.assertEquals(expectedUserId, actualUserId.value());
    }

    @Test
    public void givenEmptyUserId_whenInstantiate_shouldReturnError() {
        // given
        final var expectedUserId = " ";

        final var expectedErrorMessage = "'userId' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new UserId(expectedUserId));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}