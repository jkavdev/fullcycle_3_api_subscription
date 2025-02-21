package br.com.jkavdev.fullcycle.catalogo.application.video.list;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

public class ListVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private ListVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Test
    public void givenValidQuery_whenCallsSave_shouldReturnIt() {
        // given
        final var videos = List.of(
                Fixture.Videos.systemDesign(),
                Fixture.Videos.java21()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algooooo";
        final var expectedSort = "name";
        final var expectedRating = "L";
        final var expectedYear = 2025;
        final var expectedCategories = Set.of("asc");
        final var expectedCastMembers = Set.of("asc");
        final var expectedGenres = Set.of("asc");
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;
        final var expectedItems = videos.stream()
                .map(ListVideoUseCase.Output::from)
                .toList();

        final var aQuery =
                new ListVideoUseCase.Input(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        expectedRating,
                        expectedYear,
                        expectedCategories,
                        expectedCastMembers,
                        expectedGenres
                );

        final var pagination =
                new Pagination<>(expectedPage, expectedPerPage, videos.size(), videos);

        Mockito.when(videoGateway.findAll(ArgumentMatchers.any()))
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
