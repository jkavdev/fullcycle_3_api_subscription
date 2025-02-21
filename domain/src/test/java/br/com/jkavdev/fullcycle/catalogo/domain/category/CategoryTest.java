package br.com.jkavdev.fullcycle.catalogo.domain.category;

import br.com.jkavdev.fullcycle.catalogo.domain.UnitTest;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class CategoryTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallWith_thenInstantiateACategory() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "Filmes";
        final var expectedDescription = "Melhor categoria";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();

        // when
        final var actualCategory =
                Category.with(expectedId, expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, expectedDates);

        //  then
        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertEquals(expectedDates, actualCategory.createdAt());
        Assertions.assertEquals(expectedDates, actualCategory.updatedAt());
        Assertions.assertEquals(expectedDates, actualCategory.deletedAt());
    }

    @Test
    public void givenAValidParams_whenCallWithCategory_thenInstantiateACategory() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "Filmes";
        final var expectedDescription = "Melhor categoria";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();

        // when
        final var aCategory =
                Category.with(expectedId, expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, expectedDates);

        // then
        final var actualCategory = Category.with(aCategory);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(aCategory.id(), actualCategory.id());
        Assertions.assertEquals(aCategory.name(), actualCategory.name());
        Assertions.assertEquals(aCategory.description(), actualCategory.description());
        Assertions.assertEquals(aCategory.active(), actualCategory.active());
        Assertions.assertEquals(aCategory.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(aCategory.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aCategory.deletedAt(), actualCategory.deletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewACategoryAndValidate_thenShouldReceiveAnError() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final String expectedName = null;
        final var expectedDescription = "Melhor categoria";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualCategory =
                Category.with(expectedId, expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, expectedDates);

        // when
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallNewACategoryAndValidate_thenShouldReceiveAnError() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "  ";
        final var expectedDescription = "Melhor categoria";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualCategory =
                Category.with(expectedId, expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, expectedDates);

        // when
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNullId_whenCallNewACategoryAndValidate_thenShouldReceiveAnError() {
        // given
        final String expectedId = null;
        final var expectedName = "Filmes";
        final var expectedDescription = "Melhor categoria";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var actualCategory =
                Category.with(expectedId, expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, expectedDates);

        // when
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyId_whenCallNewACategoryAndValidate_thenShouldReceiveAnError() {
        // given
        final var expectedId = "  ";
        final var expectedName = "Filmes";
        final var expectedDescription = "Melhor categoria";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var actualCategory =
                Category.with(expectedId, expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, expectedDates);

        // when
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

}
