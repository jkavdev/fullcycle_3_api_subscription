package br.com.jkavdev.fullcycle.catalogo.application.genre.list;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

public class ListGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private ListGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenValidQuery_whenCallsSave_shouldReturnIt() {
        // given
        final var genres = List.of(
                Fixture.Genres.business(),
                Fixture.Genres.tech()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algooooo";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;
        final var expectedItems = genres.stream()
                .map(ListGenreUseCase.Output::from)
                .toList();
        final var expectedCategories = Set.of("c1");

        final var aQuery =
                new ListGenreUseCase.Input(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        final var pagination =
                new Pagination<>(expectedPage, expectedPerPage, genres.size(), genres);

        Mockito.when(genreGateway.findAll(ArgumentMatchers.any()))
                .thenReturn(pagination);

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedItemsCount, actualOutput.metadata().total());
        Assertions.assertTrue(
                expectedItems.size() == actualOutput.data().size()
                        && expectedItems.containsAll(actualOutput.data())
        );

    }

}
