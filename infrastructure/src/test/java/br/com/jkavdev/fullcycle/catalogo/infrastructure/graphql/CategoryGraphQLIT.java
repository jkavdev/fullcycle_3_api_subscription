package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.IntegrationTest;
import br.com.jkavdev.fullcycle.catalogo.WebGraphQLSecurityInterceptor;
import br.com.jkavdev.fullcycle.catalogo.application.category.list.ListCategoryOutput;
import br.com.jkavdev.fullcycle.catalogo.application.category.list.ListCategoryUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.save.SaveCategoryUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.security.Roles;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.server.WebGraphQlHandler;
import org.springframework.graphql.test.tester.WebGraphQlTester;

import java.util.List;

@IntegrationTest
public class CategoryGraphQLIT {

    @MockBean
    private ListCategoryUseCase listCategoryUseCase;

    @MockBean
    private SaveCategoryUseCase saveCategoryUseCase;

    @Autowired
    private WebGraphQlHandler webGraphQlHandler;

    @Autowired
    private WebGraphQLSecurityInterceptor interceptor;

    @Test
    public void givenAnonymousUser_whenQueries_shouldReturnUnauthorized() {
        // given
        interceptor.setAuthorities();
        final var document = "query categories { categories { id } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().expect(err ->
                        "Unauthorized".equals(err.getMessage())
                                && "categories".equals(err.getPath()
                        )).verify();

        // when
        // then
    }

    @Test
    public void givenUserWithAdminRole_whenQueries_shouldReturnResult() {
        // given
        interceptor.setAuthorities(Roles.ROLE_ADMIN);

        final var categories = List.of(
                ListCategoryOutput.from(Fixture.Categories.aulas()),
                ListCategoryOutput.from(Fixture.Categories.lives())
        );

        final var expectedIds = categories.stream().map(ListCategoryOutput::id).toList();

        Mockito.when(listCategoryUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(0, 10, categories.size(), categories));

        final var document = "query categories { categories { id } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().verify()
                .path("categories[*].id").entityList(String.class).isEqualTo(expectedIds);

        // when
        // then
    }

    @Test
    public void givenUserWithSubscriberRole_whenQueries_shouldReturnResult() {
        // given
        interceptor.setAuthorities(Roles.ROLE_SUBSCRIBER);

        final var categories = List.of(
                ListCategoryOutput.from(Fixture.Categories.aulas()),
                ListCategoryOutput.from(Fixture.Categories.lives())
        );

        final var expectedIds = categories.stream().map(ListCategoryOutput::id).toList();

        Mockito.when(listCategoryUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(0, 10, categories.size(), categories));

        final var document = "query categories { categories { id } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().verify()
                .path("categories[*].id").entityList(String.class).isEqualTo(expectedIds);

        // when
        // then
    }

    @Test
    public void givenUserWithCategoriesRole_whenQueries_shouldReturnResult() {
        // given
        interceptor.setAuthorities(Roles.ROLE_CATEGORIES);

        final var categories = List.of(
                ListCategoryOutput.from(Fixture.Categories.aulas()),
                ListCategoryOutput.from(Fixture.Categories.lives())
        );

        final var expectedIds = categories.stream().map(ListCategoryOutput::id).toList();

        Mockito.when(listCategoryUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(0, 10, categories.size(), categories));

        final var document = "query categories { categories { id } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().verify()
                .path("categories[*].id").entityList(String.class).isEqualTo(expectedIds);

        // when
        // then
    }

}
