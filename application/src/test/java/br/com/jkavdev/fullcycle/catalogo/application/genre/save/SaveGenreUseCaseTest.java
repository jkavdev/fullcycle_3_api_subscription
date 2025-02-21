package br.com.jkavdev.fullcycle.catalogo.application.genre.save;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.Genre;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Set;
import java.util.UUID;

public class SaveGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private SaveGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenValidInput_whenCallsSave_shouldPersistIt() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "qualquerNome";
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();

        Mockito.when(genreGateway.save(ArgumentMatchers.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var input =
                new SaveGenreUseCase.Input(expectedId, expectedName, expectedIsActive, expectedCategories,
                        expectedDates, expectedDates, expectedDates);

        final var actualOutput = useCase.execute(input);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId, actualOutput.id());

        final var captor = ArgumentCaptor.forClass(Genre.class);
        Mockito.verify(genreGateway, Mockito.times(1))
                .save(captor.capture());

        final var actualGenre = captor.getValue();
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
    public void givenNullCategory_whenCallsSave_shouldReturnError() {
        // given
        final SaveGenreUseCase.Input input = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'SaveGenreUseCase.Input' cannot be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(input));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(genreGateway, Mockito.never())
                .save(Mockito.any());

    }

    @Test
    public void givenInvalidNameCategory_whenCallsSave_shouldReturnError() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "   ";
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var input =
                new SaveGenreUseCase.Input(expectedId, expectedName, expectedIsActive, expectedCategories,
                        expectedDates, expectedDates, expectedDates);

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(input));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(genreGateway, Mockito.never())
                .save(Mockito.any());

    }

    @Test
    public void givenInvalidIdCategory_whenCallsSave_shouldReturnError() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final String expectedName = null;
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var input =
                new SaveGenreUseCase.Input(expectedId, expectedName, expectedIsActive, expectedCategories,
                        expectedDates, expectedDates, expectedDates);

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(input));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(genreGateway, Mockito.never())
                .save(Mockito.any());

    }

}
