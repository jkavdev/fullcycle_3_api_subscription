package br.com.jkavdev.fullcycle.subscription.domain.person;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    public void givenValidEmail_whenInstantiate_shouldReturnValueObject() {
        // given
        final var expectedEmail = "qualquerNome@email.com";

        // when
        final var actualEmail = new Email(expectedEmail);

        // then
        Assertions.assertEquals(expectedEmail, actualEmail.value());
    }

    @Test
    public void givenEmptyEmail_whenInstantiate_shouldReturnError() {
        // given
        final String expectedEmail = " ";

        final var expectedErrorMessage = "'email' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new Email(expectedEmail));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullEmail_whenInstantiate_shouldReturnError() {
        // given
        final String expectedEmail = null;

        final var expectedErrorMessage = "'email' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new Email(expectedEmail));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidEmail_whenInstantiate_shouldReturnError() {
        // given
        final String expectedEmail = "qualquerEmail";

        final var expectedErrorMessage = "'email' is invalid";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new Email(expectedEmail));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}