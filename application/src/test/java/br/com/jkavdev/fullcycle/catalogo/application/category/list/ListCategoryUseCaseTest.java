package br.com.jkavdev.fullcycle.catalogo.application.category.list;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategoryGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategorySearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class ListCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private ListCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenValidCategory_whenCallsSave_shouldPersistIt() {
        // given
        final var categories = List.of(
                Fixture.Categories.aulas(),
                Fixture.Categories.lives()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algooooo";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;
        final var expectedItems = categories.stream()
                .map(ListCategoryOutput::from)
                .toList();

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var pagination =
                new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        Mockito.when(categoryGateway.findAll(ArgumentMatchers.any()))
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
