package br.com.jkavdev.fullcycle.catalogo.domain.castmember;

import br.com.jkavdev.fullcycle.catalogo.domain.UnitTest;
import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class CastMemberTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallWith_thenInstantiateACastMember() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "QualquerMembro";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();

        // when
        final var actualMember =
                CastMember.with(expectedId, expectedName, expectedType, expectedDates, expectedDates);

        //  then
        Assertions.assertNotNull(actualMember);
        Assertions.assertEquals(expectedId, actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertEquals(expectedDates, actualMember.createdAt());
        Assertions.assertEquals(expectedDates, actualMember.updatedAt());
    }

    @Test
    public void givenAValidParams_whenCallWithCastMember_thenInstantiateACastMember() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "QualquerMembro";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();

        // when
        final var aMember =
                CastMember.with(expectedId, expectedName, expectedType, expectedDates, expectedDates);

        // then
        final var actualMember = CastMember.with(aMember);

        Assertions.assertNotNull(actualMember);
        Assertions.assertEquals(expectedId, actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertEquals(expectedDates, actualMember.createdAt());
        Assertions.assertEquals(expectedDates, actualMember.updatedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallWithAndValidate_thenShouldReceiveAnError() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualMember =
                CastMember.with(expectedId, expectedName, expectedType, expectedDates, expectedDates);

        // when
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallWithAndValidate_thenShouldReceiveAnError() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "  ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualMember =
                CastMember.with(expectedId, expectedName, expectedType, expectedDates, expectedDates);

        // when
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNullId_whenCallWithAndValidate_thenShouldReceiveAnError() {
        // given
        final String expectedId = null;
        final var expectedName = "QualquerMembro";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var actualMember =
                CastMember.with(expectedId, expectedName, expectedType, expectedDates, expectedDates);

        // when
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyId_whenCallWithAndValidate_thenShouldReceiveAnError() {
        // given
        final var expectedId = "  ";
        final var expectedName = "QualquerMembro";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var actualMember =
                CastMember.with(expectedId, expectedName, expectedType, expectedDates, expectedDates);

        // when
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNullType_whenCallWithAndValidate_thenShouldReceiveAnError() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "QualquerMembro";
        final CastMemberType expectedType = null;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualMember =
                CastMember.with(expectedId, expectedName, expectedType, expectedDates, expectedDates);

        // when
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

}
