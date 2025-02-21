package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.IntegrationTest;
import br.com.jkavdev.fullcycle.catalogo.WebGraphQLSecurityInterceptor;
import br.com.jkavdev.fullcycle.catalogo.application.genre.list.ListGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.genre.save.SaveGenreUseCase;
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
public class GenreGraphQLIT {

    @MockBean
    private ListGenreUseCase listGenreUseCase;

    @MockBean
    private SaveGenreUseCase saveGenreUseCase;

    @Autowired
    private WebGraphQlHandler webGraphQlHandler;

    @Autowired
    private WebGraphQLSecurityInterceptor interceptor;

    @Test
    public void givenAnonymousUser_whenQueries_shouldReturnUnauthorized() {
        // given
        interceptor.setAuthorities();
        final var document = "query genres { genres { id } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().expect(err ->
                        "Unauthorized".equals(err.getMessage())
                                && "genres".equals(err.getPath()
                        )).verify();

        // when
        // then
    }

    @Test
    public void givenUserWithAdminRole_whenQueries_shouldReturnResult() {
        // given
        interceptor.setAuthorities(Roles.ROLE_ADMIN);

        final var members = List.of(
                ListGenreUseCase.Output.from(Fixture.Genres.tech()),
                ListGenreUseCase.Output.from(Fixture.Genres.business())
        );

        final var expectedIds = members.stream().map(ListGenreUseCase.Output::id).toList();

        Mockito.when(listGenreUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(0, 10, members.size(), members));

        final var document = "query genres { genres { id } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().verify()
                .path("genres[*].id").entityList(String.class).isEqualTo(expectedIds);

        // when
        // then
    }

    @Test
    public void givenUserWithSubscriberRole_whenQueries_shouldReturnResult() {
        // given
        interceptor.setAuthorities(Roles.ROLE_SUBSCRIBER);

        final var members = List.of(
                ListGenreUseCase.Output.from(Fixture.Genres.tech()),
                ListGenreUseCase.Output.from(Fixture.Genres.business())
        );

        final var expectedIds = members.stream().map(ListGenreUseCase.Output::id).toList();

        Mockito.when(listGenreUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(0, 10, members.size(), members));

        final var document = "query genres { genres { id } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().verify()
                .path("genres[*].id").entityList(String.class).isEqualTo(expectedIds);

        // when
        // then
    }

    @Test
    public void givenUserWithGenresRole_whenQueries_shouldReturnResult() {
        // given
        interceptor.setAuthorities(Roles.ROLE_GENRES);

        final var members = List.of(
                ListGenreUseCase.Output.from(Fixture.Genres.tech()),
                ListGenreUseCase.Output.from(Fixture.Genres.business())
        );

        final var expectedIds = members.stream().map(ListGenreUseCase.Output::id).toList();

        Mockito.when(listGenreUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(0, 10, members.size(), members));

        final var document = "query genres { genres { id } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().verify()
                .path("genres[*].id").entityList(String.class).isEqualTo(expectedIds);

        // when
        // then
    }

}
