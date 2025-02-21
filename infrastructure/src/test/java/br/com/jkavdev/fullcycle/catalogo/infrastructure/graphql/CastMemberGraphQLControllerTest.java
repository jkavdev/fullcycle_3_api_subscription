package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.GraphQLControllerTest;
import br.com.jkavdev.fullcycle.catalogo.application.castmember.list.ListCastMemberOutput;
import br.com.jkavdev.fullcycle.catalogo.application.castmember.list.ListCastMemberUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.castmember.save.SaveCastMemberUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMember;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberType;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.IdUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.GqlCastMemberPresenter;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.models.GqlCastMember;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@GraphQLControllerTest(controllers = CastMemberGraphQLController.class)
public class CastMemberGraphQLControllerTest {

    @MockBean
    private ListCastMemberUseCase listCastMemberUseCase;

    @MockBean
    private SaveCastMemberUseCase saveCastMemberUseCase;

    @Autowired
    private GraphQlTester graphql;

    @Test
    public void givenDefaultArgumentsWhenCallsListCastMembersShouldReturn() {
        // given
        final var members = List.of(
                ListCastMemberOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMemberOutput.from(Fixture.CastMembers.wesley())
        );

        final var expectedMembers = members.stream()
                .map(GqlCastMemberPresenter::present)
                .toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";

        Mockito.when(listCastMemberUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, members.size(), members));

        final var query = """
                {
                 castMembers {
                  id
                  name
                  type
                  createdAt
                  updatedAt
                 }
                }
                """;

        // when
        final var res = graphql.document(query).execute();

        final var actualMembers = res.path("castMembers")
                .entityList(GqlCastMember.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedMembers.size() == actualMembers.size()
                        && expectedMembers.containsAll(actualMembers)
        );

        final var captor = ArgumentCaptor.forClass(CastMemberSearchQuery.class);
        Mockito.verify(listCastMemberUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

    @Test
    public void givenCustomArgumentsWhenCallsListCastMembersShouldReturn() {
        // given
        final var members = List.of(
                ListCastMemberOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMemberOutput.from(Fixture.CastMembers.wesley())
        );

        final var expectedMembers = members.stream()
                .map(GqlCastMemberPresenter::present)
                .toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";

        Mockito.when(listCastMemberUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, members.size(), members));

        final var query = """
                {
                 castMembers(search: "%s", page: %s, perPage: %s, sort: "%s", direction: "%s") {
                  id
                  name
                  type
                  createdAt
                  updatedAt
                 }
                }
                """.formatted(expectedSearch, expectedPage, expectedPerPage, expectedSort, expectedDirection);

        // when
        final var res = graphql.document(query).execute();

        final var actualCategories = res.path("castMembers")
                .entityList(GqlCastMember.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedMembers.size() == actualCategories.size()
                        && expectedMembers.containsAll(actualCategories)
        );

        final var captor = ArgumentCaptor.forClass(CastMemberSearchQuery.class);
        Mockito.verify(listCastMemberUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

    @Test
    public void givenCustomArgumentsWithVariablesWhenCallsListCastMembersShouldReturn() {
        // given
        final var members = List.of(
                ListCastMemberOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMemberOutput.from(Fixture.CastMembers.wesley())
        );

        final var expectedMembers = members.stream()
                .map(GqlCastMemberPresenter::present)
                .toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";

        Mockito.when(listCastMemberUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, members.size(), members));

        final var query = """
                query AllCastMembers($search: String, $page: Int, $perPage: Int, $sort: String, $direction: String){
                
                    castMembers(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction) {
                      id
                      name
                      type
                      createdAt
                      updatedAt
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

        final var actualMembers = res.path("castMembers")
                .entityList(GqlCastMember.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedMembers.size() == actualMembers.size()
                        && expectedMembers.containsAll(actualMembers)
        );

        final var captor = ArgumentCaptor.forClass(CastMemberSearchQuery.class);
        Mockito.verify(listCastMemberUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

    @Test
    public void givenCastMemberInputWhenCallsSaveCastMemberMutationShouldPersistAndReturnIt() {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedName = "qualquerNome";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();

        final var input = Map.of(
                "id", expectedId,
                "name", expectedName,
                "type", expectedType,
                "createdAt", expectedCreatedAt.toString(),
                "updatedAt", expectedUpdatedAt.toString()
        );

        final var query = """
                mutation SaveCastMember($input: CastMemberInput!){
                    castMember: saveCastMember(input: $input) {
                        id
                        name
                        type
                        createdAt
                        updatedAt
                    }
                }
                """;

        Mockito.doAnswer(AdditionalAnswers.returnsFirstArg())
                .when(saveCastMemberUseCase)
                .execute(ArgumentMatchers.any());

        // when
        graphql.document(query)
                .variable("input", input)
                .execute()
                .path("castMember.id").entity(String.class).isEqualTo(expectedId)
                .path("castMember.name").entity(String.class).isEqualTo(expectedName)
                .path("castMember.type").entity(CastMemberType.class).isEqualTo(expectedType)
                .path("castMember.createdAt").entity(Instant.class).isEqualTo(expectedCreatedAt)
                .path("castMember.updatedAt").entity(Instant.class).isEqualTo(expectedUpdatedAt)
        ;

        // then
        final var captor = ArgumentCaptor.forClass(CastMember.class);
        Mockito.verify(saveCastMemberUseCase, Mockito.times(1))
                .execute(captor.capture());

        final var actualMember = captor.getValue();
        Assertions.assertEquals(expectedId, actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertEquals(expectedCreatedAt, actualMember.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualMember.updatedAt());
    }

}
