package br.com.jkavdev.fullcycle.catalogo.application.castmember.list;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class ListCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private ListCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenValidQuery_whenCallsListCastMembers_shouldReturnIt() {
        // given
        final var members = List.of(
                Fixture.CastMembers.gabriel(),
                Fixture.CastMembers.wesley()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algooooo";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;
        final var expectedItems = members.stream()
                .map(ListCastMemberOutput::from)
                .toList();

        final var aQuery =
                new CastMemberSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var pagination =
                new Pagination<>(expectedPage, expectedPerPage, members.size(), members);

        Mockito.when(castMemberGateway.findAll(ArgumentMatchers.any()))
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
