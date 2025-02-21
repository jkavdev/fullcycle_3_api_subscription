package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.IntegrationTest;
import br.com.jkavdev.fullcycle.catalogo.WebGraphQLSecurityInterceptor;
import br.com.jkavdev.fullcycle.catalogo.application.castmember.list.ListCastMemberOutput;
import br.com.jkavdev.fullcycle.catalogo.application.castmember.list.ListCastMemberUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.castmember.save.SaveCastMemberUseCase;
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
public class CastMemberGraphQLIT {

    @MockBean
    private ListCastMemberUseCase listCastMemberUseCase;

    @MockBean
    private SaveCastMemberUseCase saveCastMemberUseCase;

    @Autowired
    private WebGraphQlHandler webGraphQlHandler;

    @Autowired
    private WebGraphQLSecurityInterceptor interceptor;

    @Test
    public void givenAnonymousUser_whenQueries_shouldReturnUnauthorized() {
        // given
        interceptor.setAuthorities();
        final var document = "query castMembers { castMembers { id } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().expect(err ->
                        "Unauthorized".equals(err.getMessage())
                                && "castMembers".equals(err.getPath()
                        )).verify();

        // when
        // then
    }

    @Test
    public void givenUserWithAdminRole_whenQueries_shouldReturnResult() {
        // given
        interceptor.setAuthorities(Roles.ROLE_ADMIN);

        final var members = List.of(
                ListCastMemberOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMemberOutput.from(Fixture.CastMembers.wesley())
        );

        final var expectedIds = members.stream().map(ListCastMemberOutput::id).toList();

        Mockito.when(listCastMemberUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(0, 10, members.size(), members));

        final var document = "query castMembers { castMembers { id } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().verify()
                .path("castMembers[*].id").entityList(String.class).isEqualTo(expectedIds);

        // when
        // then
    }

    @Test
    public void givenUserWithSubscriberRole_whenQueries_shouldReturnResult() {
        // given
        interceptor.setAuthorities(Roles.ROLE_SUBSCRIBER);

        final var members = List.of(
                ListCastMemberOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMemberOutput.from(Fixture.CastMembers.wesley())
        );

        final var expectedIds = members.stream().map(ListCastMemberOutput::id).toList();

        Mockito.when(listCastMemberUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(0, 10, members.size(), members));

        final var document = "query castMembers { castMembers { id } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().verify()
                .path("castMembers[*].id").entityList(String.class).isEqualTo(expectedIds);

        // when
        // then
    }

    @Test
    public void givenUserWithCastMembersRole_whenQueries_shouldReturnResult() {
        // given
        interceptor.setAuthorities(Roles.ROLE_CAST_MEMBERS);

        final var members = List.of(
                ListCastMemberOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMemberOutput.from(Fixture.CastMembers.wesley())
        );

        final var expectedIds = members.stream().map(ListCastMemberOutput::id).toList();

        Mockito.when(listCastMemberUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(0, 10, members.size(), members));

        final var document = "query castMembers { castMembers { id } }";
        final var graphQlTester = WebGraphQlTester.create(webGraphQlHandler);

        graphQlTester.document(document).execute()
                .errors().verify()
                .path("castMembers[*].id").entityList(String.class).isEqualTo(expectedIds);

        // when
        // then
    }

}
