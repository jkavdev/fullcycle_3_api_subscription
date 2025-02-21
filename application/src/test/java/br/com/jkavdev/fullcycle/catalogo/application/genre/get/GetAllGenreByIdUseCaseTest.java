package br.com.jkavdev.fullcycle.catalogo.application.genre.get;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.Genre;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class GetAllGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetAllGenreByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenValidQuery_whenCallsAllById_shouldReturnIt() {
        // given
        final var genres = List.of(
                Fixture.Genres.business(),
                Fixture.Genres.tech()
        );

        final var expectedIds = genres.stream().map(Genre::id).collect(Collectors.toSet());

        final var expectedItems = genres.stream()
                .map(GetAllGenreByIdUseCase.Output::new)
                .toList();

        Mockito.when(genreGateway.findAllById(ArgumentMatchers.any()))
                .thenReturn(genres);

        // when
        final var actualOutput = useCase.execute(new GetAllGenreByIdUseCase.Input(expectedIds));

        // then
        Assertions.assertTrue(
                expectedItems.size() == actualOutput.size()
                        && expectedItems.containsAll(actualOutput)
        );

        Mockito.verify(genreGateway, Mockito.times(1)).findAllById(expectedIds);

    }

    @Test
    public void givenNullIds_whenCallsAllById_shouldReturnEmpty() {
        // given
        final Set<String> expectedIds = null;

        // when
        final var actualOutput = useCase.execute(new GetAllGenreByIdUseCase.Input(expectedIds));

        // then
        Assertions.assertTrue(actualOutput.isEmpty());

        Mockito.verify(genreGateway, Mockito.never()).findAllById(ArgumentMatchers.any());

    }

}