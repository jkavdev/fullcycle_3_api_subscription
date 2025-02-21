package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.GraphQLControllerTest;
import br.com.jkavdev.fullcycle.catalogo.application.genre.list.ListGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.genre.save.SaveGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.IdUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.GqlGenrePresenter;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GqlGenre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;
import java.util.Map;
import java.util.Set;

@GraphQLControllerTest(controllers = GenreGraphQLController.class)
public class GenreGraphQLControllerTest {

    private static final ParameterizedTypeReference<Set<String>> CATEGORIES_TYPE = new ParameterizedTypeReference<>() {
    };

    @MockBean
    private ListGenreUseCase listGenreUseCase;

    @MockBean
    private SaveGenreUseCase saveGenreUseCase;

    @Autowired
    private GraphQlTester graphql;

    @Test
    public void givenDefaultArgumentsWhenCallsListGenresShouldReturn() {
        // given
        final var genres = List.of(
                ListGenreUseCase.Output.from(Fixture.Genres.business()),
                ListGenreUseCase.Output.from(Fixture.Genres.tech())
        );

        final var expectedGenres = genres.stream()
                .map(GqlGenrePresenter::present)
                .toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";
        final var expectedCategories = Set.of();

        Mockito.when(listGenreUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(
                        new Pagination<>(expectedPage, expectedPerPage, genres.size(), genres
                        ));

        final var query = """
                {
                 genres {
                  id
                  name
                  active
                  categories
                  createdAt
                  updatedAt
                  deletedAt
                 }
                }
                """;

        // when
        final var res = graphql.document(query).execute();

        final var actualGenres = res.path("genres")
                .entityList(GqlGenre.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedGenres.size() == actualGenres.size()
                        && expectedGenres.containsAll(actualGenres)
        );

        final var captor = ArgumentCaptor.forClass(ListGenreUseCase.Input.class);
        Mockito.verify(listGenreUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
        Assertions.assertEquals(expectedCategories, actualQuery.categories());
    }

    @Test
    public void givenCustomArgumentsWhenCallsListGenresShouldReturn() {
        // given
        final var genres = List.of(
                ListGenreUseCase.Output.from(Fixture.Genres.business()),
                ListGenreUseCase.Output.from(Fixture.Genres.tech())
        );

        final var expectedGenres = genres.stream()
                .map(GqlGenrePresenter::present)
                .toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";
        final var expectedCategories = Set.of();

        Mockito.when(listGenreUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(
                        new Pagination<>(expectedPage, expectedPerPage, genres.size(), genres
                        ));

        final var query = """
                {
                 genres(search: "%s", page: %s, perPage: %s, sort: "%s", direction: "%s") {
                  id
                  name
                  active
                  categories
                  createdAt
                  updatedAt
                  deletedAt
                 }
                }
                """.formatted(expectedSearch, expectedPage, expectedPerPage, expectedSort, expectedDirection);

        // when
        final var res = graphql.document(query).execute();

        final var actualGenres = res.path("genres")
                .entityList(GqlGenre.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedGenres.size() == actualGenres.size()
                        && expectedGenres.containsAll(actualGenres)
        );

        final var captor = ArgumentCaptor.forClass(ListGenreUseCase.Input.class);
        Mockito.verify(listGenreUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
        Assertions.assertEquals(expectedCategories, actualQuery.categories());
    }

    @Test
    public void givenCustomArgumentsWithVariablesWhenCallsListGenresShouldReturn() {
        // given
        final var genres = List.of(
                ListGenreUseCase.Output.from(Fixture.Genres.business()),
                ListGenreUseCase.Output.from(Fixture.Genres.tech())
        );

        final var expectedGenres = genres.stream()
                .map(GqlGenrePresenter::present)
                .toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";
        final var expectedCategories = Set.of("c1");

        Mockito.when(listGenreUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(
                        new Pagination<>(expectedPage, expectedPerPage, genres.size(), genres
                        ));

        final var query = """
                query AllGenres($search: String, $page: Int, $perPage: Int, $sort: String, $direction: String, $categories: [String]){
                
                    genres(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction, categories: $categories) {
                      id
                      name
                      active
                      categories
                      createdAt
                      updatedAt
                      deletedAt
                    }
                }
                """;

        // when
        final var res = graphql.document(query)
                .variable("search", expectedSearch)
                .variable("page", expectedPage)
                .variable("perPage", expectedPerPage)
                .variable("sort", expectedSort)
                .variable("direction", expectedDirection)
                .variable("categories", expectedCategories)
                .execute();

        final var actualGenres = res.path("genres")
                .entityList(GqlGenre.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedGenres.size() == actualGenres.size()
                        && expectedGenres.containsAll(actualGenres)
        );

        final var captor = ArgumentCaptor.forClass(ListGenreUseCase.Input.class);
        Mockito.verify(listGenreUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
        Assertions.assertEquals(expectedCategories, actualQuery.categories());
    }

    @Test
    public void givenGenreInput_whenCallsSaveGenreMutation_shouldPersistAndReturn() {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedName = "qualquerNome";
        final var expectedActive = false;
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedDate = InstantUtils.now();

        final var input = Map.of(
                "id", expectedId,
                "name", expectedName,
                "active", expectedActive,
                "categories", expectedCategories,
                "createdAt", expectedDate.toString(),
                "updatedAt", expectedDate.toString(),
                "deletedAt", expectedDate.toString()
        );

        // indicando apenas alguns campos como retorno
        final var query = """
                mutation SaveGenre($input: GenreInput!){
                    genre: saveGenre(input: $input) {
                      id
                    }
                }
                """;

        // retornando o proprio argumento como retorno do metodo
        Mockito.doReturn(new SaveGenreUseCase.Output(expectedId))
                .when(saveGenreUseCase)
                .execute(ArgumentMatchers.any());

        // when
        graphql.document(query)
                .variable("input", input)
                .execute()
                // da pra testar o retorno do graphql
                .path("genre.id").entity(String.class).isEqualTo(expectedId);

        // then
        final var captor = ArgumentCaptor.forClass(SaveGenreUseCase.Input.class);
        Mockito.verify(saveGenreUseCase, Mockito.times(1))
                .execute(captor.capture());

        final var actualGenre = captor.getValue();
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedActive, actualGenre.active());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(expectedDate, actualGenre.createdAt());
        Assertions.assertEquals(expectedDate, actualGenre.updatedAt());
        Assertions.assertEquals(expectedDate, actualGenre.deletedAt());
    }

    @Test
    public void givenActiveGenreInputWithoutDeletedAt_whenCallsSaveGenreMutation_shouldPersistAndReturn() {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedName = "qualquerNome";
        final var expectedActive = false;
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedDate = InstantUtils.now();

        final var input = Map.of(
                "id", expectedId,
                "name", expectedName,
                "active", expectedActive,
                "categories", expectedCategories,
                "createdAt", expectedDate.toString(),
                "updatedAt", expectedDate.toString()
        );

        // indicando apenas alguns campos como retorno
        final var query = """
                mutation SaveGenre($input: GenreInput!){
                    genre: saveGenre(input: $input) {
                      id
                    }
                }
                """;

        // retornando o proprio argumento como retorno do metodo
        Mockito.doReturn(new SaveGenreUseCase.Output(expectedId))
                .when(saveGenreUseCase)
                .execute(ArgumentMatchers.any());

        // when
        graphql.document(query)
                .variable("input", input)
                .execute()
                // da pra testar o retorno do graphql
                .path("genre.id").entity(String.class).isEqualTo(expectedId);

        // then
        final var captor = ArgumentCaptor.forClass(SaveGenreUseCase.Input.class);
        Mockito.verify(saveGenreUseCase, Mockito.times(1))
                .execute(captor.capture());

        final var actualGenre = captor.getValue();
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedActive, actualGenre.active());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(expectedDate, actualGenre.createdAt());
        Assertions.assertEquals(expectedDate, actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

}
