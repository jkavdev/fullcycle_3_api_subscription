package br.com.jkavdev.fullcycle.subscription.domain.plan;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlanIdTest {

    @Test
    public void givenValue_whenInstantiate_shouldReturnValueObject() {
        // given
        final var expectedID = 123L;

        // when
        final var actualID = new PlanId(expectedID);

        // then
        Assertions.assertEquals(expectedID, actualID.value());
    }

    @Test
    public void givenEmpty_whenCallsEmpty_shouldReturnOK() {
        // given
        final Long expectedID = null;

        // when
        final var actualID = PlanId.empty();

        // then
        Assertions.assertEquals(expectedID, actualID.value());
    }

    @Test
    public void givenNullId_whenInstantiate_shouldReturnError() {
        // given
        final Long expectedID = null;

        final var expectedErrorMessage = "'planId' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> new PlanId(expectedID));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenEmpty_whenCallsEquals_shouldReturnTrue() {
        // given
        // when
        final var actualOne = PlanId.empty();
        final var actualTwo = PlanId.empty();

        // then
        Assertions.assertTrue(actualOne.equals(actualTwo));
    }

}