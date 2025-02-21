package br.com.jkavdev.fullcycle.catalogo.domain.genre;

import br.com.jkavdev.fullcycle.catalogo.domain.UnitTest;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

public class GenreTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallWith_thenInstantiateAGenre() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "Filmes";
        final var expectedIsActive = true;
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedDates = InstantUtils.now();

        // when
        final var actualGenre =
                Genre.with(expectedId, expectedName, expectedIsActive, expectedCategories, expectedDates, expectedDates, expectedDates);

        //  then
        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(expectedDates, actualGenre.createdAt());
        Assertions.assertEquals(expectedDates, actualGenre.updatedAt());
        Assertions.assertEquals(expectedDates, actualGenre.deletedAt());
    }

    @Test
    public void givenAValidParams_whenCallWithGenre_thenInstantiateAGenre() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "Filmes";
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();

        // when
        final var aGenre =
                Genre.with(expectedId, expectedName, expectedIsActive, expectedCategories, expectedDates, expectedDates, expectedDates);

        // then
        final var actualGenre = Genre.with(aGenre);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(expectedDates, actualGenre.createdAt());
        Assertions.assertEquals(expectedDates, actualGenre.updatedAt());
        Assertions.assertEquals(expectedDates, actualGenre.deletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewAGenreAndValidate_thenShouldReceiveAnError() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final String expectedName = null;
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Genre.with(expectedId, expectedName, expectedIsActive, expectedCategories, expectedDates, expectedDates, expectedDates));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenNullCategories_whenCallWith_thenInstantiateAGenreWithEmptyCategories() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "qualquerNome";
        final Set<String> expectedCategories = null;
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();

        // when
        final var aGenre =
                Genre.with(expectedId, expectedName, expectedIsActive, expectedCategories, expectedDates, expectedDates, expectedDates);

        // then
        final var actualGenre = Genre.with(aGenre);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertNotNull(actualGenre.categories());
        Assertions.assertTrue(actualGenre.categories().isEmpty());
        Assertions.assertEquals(expectedDates, actualGenre.createdAt());
        Assertions.assertEquals(expectedDates, actualGenre.updatedAt());
        Assertions.assertEquals(expectedDates, actualGenre.deletedAt());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallNewAGenreAndValidate_thenShouldReceiveAnError() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "  ";
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class, () ->
                        Genre.with(expectedId, expectedName, expectedIsActive, expectedCategories, expectedDates, expectedDates, expectedDates)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNullId_whenCallNewAGenreAndValidate_thenShouldReceiveAnError() {
        // given
        final String expectedId = null;
        final var expectedName = "Filmes";
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class, () ->
                        Genre.with(expectedId, expectedName, expectedIsActive, expectedCategories, expectedDates, expectedDates, expectedDates)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyId_whenCallNewAGenreAndValidate_thenShouldReceiveAnError() {
        // given
        final var expectedId = "  ";
        final var expectedName = "Filmes";
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class, () ->
                        Genre.with(expectedId, expectedName, expectedIsActive, expectedCategories, expectedDates, expectedDates, expectedDates)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

}
