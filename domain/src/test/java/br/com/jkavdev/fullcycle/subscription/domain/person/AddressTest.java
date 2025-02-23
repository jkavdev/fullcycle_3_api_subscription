package br.com.jkavdev.fullcycle.subscription.domain.person;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AddressTest {

    @Test
    public void givenValidParams_whenInstantiate_shouldReturnValueObject() {
        // given
        final var expectedZipcode = "qualquerZipcode";
        final var expectedNumber = "qualquerNumber";
        final var expectedComplement = "qualquerComplement";
        final var expectedCountry = "qualquerCountry";

        // when
        final var actualAddress =
                new Address(expectedZipcode, expectedNumber, expectedComplement, expectedCountry);

        // then
        Assertions.assertEquals(expectedZipcode, actualAddress.zipcode());
        Assertions.assertEquals(expectedNumber, actualAddress.number());
        Assertions.assertEquals(expectedComplement, actualAddress.complement());
        Assertions.assertEquals(expectedCountry, actualAddress.country());
    }

    @Test
    public void givenInvalidZipcode_whenInstantiate_shouldReturnError() {
        // given
        final String expectedZipcode = null;
        final var expectedNumber = "qualquerNumber";
        final var expectedComplement = "qualquerComplement";
        final var expectedCountry = "qualquerCountry";

        final var expectedErrorMessage = "'zipcode' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new Address(expectedZipcode, expectedNumber, expectedComplement, expectedCountry));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidNumber_whenInstantiate_shouldReturnError() {
        // given
        final var expectedZipcode = "qualquerZipcode";
        final String expectedNumber = null;
        final var expectedComplement = "qualquerComplement";
        final var expectedCountry = "qualquerCountry";

        final var expectedErrorMessage = "'number' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new Address(expectedZipcode, expectedNumber, expectedComplement, expectedCountry));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidCountry_whenInstantiate_shouldReturnError() {
        // given
        final var expectedZipcode = "qualquerZipcode";
        final var expectedNumber = "qualquerNumber";
        final var expectedComplement = "qualquerComplement";
        final String expectedCountry = null;

        final var expectedErrorMessage = "'country' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new Address(expectedZipcode, expectedNumber, expectedComplement, expectedCountry));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenEmptyComplement_whenInstantiate_shouldReturnValueObject() {
        // given
        final var expectedZipcode = "qualquerZipcode";
        final var expectedNumber = "qualquerNumber";
        final String expectedComplement = null;
        final var expectedCountry = "qualquerCountry";

        // when
        // then
        Assertions.assertDoesNotThrow(
                () -> new Address(expectedZipcode, expectedNumber, expectedComplement, expectedCountry));
    }

}