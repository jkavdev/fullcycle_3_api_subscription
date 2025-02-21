package br.com.jkavdev.fullcycle.catalogo.application.genre.delete;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class DeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenValidId_whenCallsDelete_shouldBeOk() {
        // given
        final var business = Fixture.Genres.business();
        final var expectedId = business.id();

        Mockito.doNothing()
                .when(genreGateway)
                .deleteById(ArgumentMatchers.anyString());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(new DeleteGenreUseCase.Input(expectedId)));

        // then
        Mockito.verify(genreGateway, Mockito.times(1)).deleteById(expectedId);

    }

    @Test
    public void givenNullInput_whenCallsDelete_shouldBeOk() {
        // given
        final DeleteGenreUseCase.Input expectedInput = null;

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedInput));

        // then
        Mockito.verify(genreGateway, Mockito.never()).deleteById(Mockito.anyString());

    }

    @Test
    public void givenInvalidId_whenCallsDelete_shouldBeOk() {
        // given
        final String expectedId = null;

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(new DeleteGenreUseCase.Input(expectedId)));

        // then
        Mockito.verify(genreGateway, Mockito.never()).deleteById(expectedId);

    }

}
