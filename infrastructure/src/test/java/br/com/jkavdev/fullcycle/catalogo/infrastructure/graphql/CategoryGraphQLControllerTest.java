package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.GraphQLControllerTest;
import br.com.jkavdev.fullcycle.catalogo.application.category.list.ListCategoryOutput;
import br.com.jkavdev.fullcycle.catalogo.application.category.list.ListCategoryUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.save.SaveCategoryUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategorySearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.IdUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.GqlCategoryPresenter;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models.GqlCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;
import java.util.Map;

// quando indicamos o controller, sera disponibilizado apenas o controle a ser testado
// senao seria disponibilizado todos os controlers disponiveis
@GraphQLControllerTest(controllers = CategoryGraphQLController.class)
public class CategoryGraphQLControllerTest {

    @MockBean
    private ListCategoryUseCase listCategoryUseCase;

    @MockBean
    private SaveCategoryUseCase saveCategoryUseCase;

    @Autowired
    private GraphQlTester graphql;

    @Test
    public void givenDefaultArgumentsWhenCallsListCategoriesShouldReturn() {
        // given
        final var categories = List.of(
                ListCategoryOutput.from(Fixture.Categories.aulas()),
                ListCategoryOutput.from(Fixture.Categories.lives())
        );

        final var expectedCategories = categories.stream()
                .map(GqlCategoryPresenter::present)
                .toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";

        Mockito.when(listCategoryUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories));

        final var query = """
                {
                 categories {
                  id
                  name
                  description
                 }
                }
                """;

        // when
        final var res = graphql.document(query).execute();

        final var actualCategories = res.path("categories")
                .entityList(GqlCategory.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedCategories.size() == actualCategories.size()
                        && expectedCategories.containsAll(actualCategories)
        );

        final var captor = ArgumentCaptor.forClass(CategorySearchQuery.class);
        Mockito.verify(listCategoryUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

    @Test
    public void givenCustomArgumentsWhenCallsListCategoriesShouldReturn() {
        // given
        final var categories = List.of(
                ListCategoryOutput.from(Fixture.Categories.aulas()),
                ListCategoryOutput.from(Fixture.Categories.lives())
        );

        final var expectedCategories = categories.stream()
                .map(GqlCategoryPresenter::present)
                .toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";

        Mockito.when(listCategoryUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories));

        final var query = """
                {
                 categories(search: "%s", page: %s, perPage: %s, sort: "%s", direction: "%s") {
                  id
                  name
                  description
                 }
                }
                """.formatted(expectedSearch, expectedPage, expectedPerPage, expectedSort, expectedDirection);

        // when
        final var res = graphql.document(query).execute();

        final var actualCategories = res.path("categories")
                .entityList(GqlCategory.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedCategories.size() == actualCategories.size()
                        && expectedCategories.containsAll(actualCategories)
        );

        final var captor = ArgumentCaptor.forClass(CategorySearchQuery.class);
        Mockito.verify(listCategoryUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

    @Test
    public void givenCustomArgumentsWithVariablesWhenCallsListCategoriesShouldReturn() {
        // given
        final var categories = List.of(
                ListCategoryOutput.from(Fixture.Categories.aulas()),
                ListCategoryOutput.from(Fixture.Categories.lives())
        );

        final var expectedCategories = categories.stream()
                .map(GqlCategoryPresenter::present)
                .toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";

        Mockito.when(listCategoryUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories));

        final var query = """
                query AllCategories($search: String, $page: Int, $perPage: Int, $sort: String, $direction: String){
                
                    categories(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction) {
                      id
                      name
                      description
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
                .execute();

        final var actualCategories = res.path("categories")
                .entityList(GqlCategory.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedCategories.size() == actualCategories.size()
                        && expectedCategories.containsAll(actualCategories)
        );

        final var captor = ArgumentCaptor.forClass(CategorySearchQuery.class);
        Mockito.verify(listCategoryUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

    @Test
    public void givenCategoryInputWhenCallsSaveCategoryMutationShouldPersistAndReturn() {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescription";
        final var expectedActive = false;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();

        final var input = Map.of(
                "id", expectedId,
                "name", expectedName,
                "description", expectedDescription,
                "active", expectedActive,
                "createdAt", expectedCreatedAt.toString(),
                "updatedAt", expectedUpdatedAt.toString(),
                "deletedAt", expectedDeletedAt.toString()
        );

        // category: saveCategory(input: $input) {, dando um alias para o retorno da consulta
//        category: saveCategory(input: $input) {
//            id
//                    name
//            description
//        }
        // indicando apenas alguns campos como retorno
        final var query = """
                mutation SaveCategory($input: CategoryInput!){
                    category: saveCategory(input: $input) {
                        id
                        name
                        description
                    }
                }
                """;

        // retornando o proprio argumento como retorno do metodo
        Mockito.doAnswer(AdditionalAnswers.returnsFirstArg())
                .when(saveCategoryUseCase)
                .execute(ArgumentMatchers.any());

        // when
        graphql.document(query)
                .variable("input", input)
                .execute()
                // da pra testar o retorno do graphql
                .path("category.id").entity(String.class).isEqualTo(expectedId)
                .path("category.name").entity(String.class).isEqualTo(expectedName)
                .path("category.description").entity(String.class).isEqualTo(expectedDescription)
        ;

        // then
        final var captor = ArgumentCaptor.forClass(Category.class);
        Mockito.verify(saveCategoryUseCase, Mockito.times(1))
                .execute(captor.capture());

        final var actualCategory = captor.getValue();
        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedActive, actualCategory.active());
        Assertions.assertEquals(expectedCreatedAt, actualCategory.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualCategory.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, actualCategory.deletedAt());
    }

}
