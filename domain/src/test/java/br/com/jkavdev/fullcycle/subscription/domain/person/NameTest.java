package br.com.jkavdev.fullcycle.subscription.domain.person;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NameTest {

    @Test
    public void givenValidNames_whenInstantiate_shouldReturnValueObject() {
        // given
        final var expectedFirstName = "qualquerNome";
        final var expectedLastName = "qualquerSobrenome";

        // when
        final var actualName = new Name(expectedFirstName, expectedLastName);

        // then
        Assertions.assertEquals(expectedFirstName, actualName.firstname());
        Assertions.assertEquals(expectedLastName, actualName.lastname());
    }

    @Test
    public void givenInvalidFirstname_whenInstantiate_shouldReturnError() {
        // given
        final String expectedFirstName = null;
        final var expectedLastName = "qualquerSobrenome";

        final var expectedErrorMessage = "'firstname' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new Name(expectedFirstName, expectedLastName));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidLastname_whenInstantiate_shouldReturnError() {
        // given
        final var expectedFirstName = "qualquerNome";
        final String expectedLastName = null;

        final var expectedErrorMessage = "'lastname' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new Name(expectedFirstName, expectedLastName));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}