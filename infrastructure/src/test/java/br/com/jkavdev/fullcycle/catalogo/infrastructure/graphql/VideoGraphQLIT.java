package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.IntegrationTest;
import br.com.jkavdev.fullcycle.catalogo.WebGraphQLSecurityInterceptor;
import br.com.jkavdev.fullcycle.catalogo.application.video.list.ListVideoUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.video.save.SaveVideoUseCase;
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
public class VideoGraphQLIT {

    @MockBean
    private ListVideoUseCase listVideoUseCase;

    @MockBean
    private SaveVideoUseCase saveVideoUseCase;

    @Autowired
    private WebGraphQlHandler webGraphQlHandler;

    @Autowired
    private WebGraphQLSecurityInterceptor interceptor;

    @Test
    public void givenAnonymousUser_whenQueries_shouldReturnUnauthorized() {
        // given
        interceptor.setAuthorities();
        final var document = "query videos { videos { id castMembers { id } categories { id } genres { id } } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().expect(err ->
                        "Unauthorized".equals(err.getMessage())
                                && "videos".equals(err.getPath()
                        )).verify();

        // when
        // then
    }

    @Test
    public void givenUserWithAdminRole_whenQueries_shouldReturnResult() {
        // given
        interceptor.setAuthorities(Roles.ROLE_ADMIN);

        final var members = List.of(
                ListVideoUseCase.Output.from(Fixture.Videos.java21()),
                ListVideoUseCase.Output.from(Fixture.Videos.systemDesign())
        );

        final var expectedIds = members.stream().map(ListVideoUseCase.Output::id).toList();

        Mockito.when(listVideoUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(0, 10, members.size(), members));

        final var document = "query videos { videos { id castMembers { id } categories { id } genres { id } } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().verify()
                .path("videos[*].id").entityList(String.class).isEqualTo(expectedIds);

        // when
        // then
    }

    @Test
    public void givenUserWithSubscriberRole_whenQueries_shouldReturnResult() {
        // given
        interceptor.setAuthorities(Roles.ROLE_SUBSCRIBER);

        final var members = List.of(
                ListVideoUseCase.Output.from(Fixture.Videos.java21()),
                ListVideoUseCase.Output.from(Fixture.Videos.systemDesign())
        );

        final var expectedIds = members.stream().map(ListVideoUseCase.Output::id).toList();

        Mockito.when(listVideoUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(0, 10, members.size(), members));

        final var document = "query videos { videos { id castMembers { id } categories { id } genres { id } } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().verify()
                .path("videos[*].id").entityList(String.class).isEqualTo(expectedIds);

        // when
        // then
    }

    @Test
    public void givenUserWithVideosRole_whenQueries_shouldReturnResult() {
        // given
        interceptor.setAuthorities(Roles.ROLE_VIDEOS);

        final var members = List.of(
                ListVideoUseCase.Output.from(Fixture.Videos.java21()),
                ListVideoUseCase.Output.from(Fixture.Videos.systemDesign())
        );

        final var expectedIds = members.stream().map(ListVideoUseCase.Output::id).toList();

        Mockito.when(listVideoUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(0, 10, members.size(), members));

        final var document = "query videos { videos { id castMembers { id } categories { id } genres { id } } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().verify()
                .path("videos[*].id").entityList(String.class).isEqualTo(expectedIds);

        // when
        // then
    }

}
