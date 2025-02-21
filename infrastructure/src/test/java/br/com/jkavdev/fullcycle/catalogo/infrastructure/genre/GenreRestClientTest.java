package br.com.jkavdev.fullcycle.catalogo.infrastructure.genre;

import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.InternalErrorException;
import br.com.jkavdev.fullcycle.catalogo.AbstractRestClientTest;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.authentication.ClientCredentialsManager;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GenreDto;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;

import java.util.Map;

class GenreRestClientTest extends AbstractRestClientTest {

    @Autowired
    private GenreRestClient target;

    @SpyBean
    private ClientCredentialsManager credentialsManager;

    // OK
    @Test
    public void givenAGenre_whenReceive200FromServer_shouldBeOk() {
        // given
        final var business = Fixture.Genres.business();

        final var responseBody = writeValueAsString(new GenreDto(
                business.id(),
                business.name(),
                business.active(),
                business.categories(),
                business.createdAt(),
                business.updatedAt(),
                business.deletedAt()
        ));

        final var expectedToken = "qualquerAccessToken";
        Mockito.doReturn(expectedToken)
                .when(credentialsManager)
                .retrive();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/genres/%s".formatted(business.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(responseBody)
                        )
        );

        // when
        final var actualGenre = target.genreOfId(business.id()).get();

        // then
        Assertions.assertEquals(business.id(), actualGenre.id());
        Assertions.assertEquals(business.name(), actualGenre.name());
        Assertions.assertEquals(business.active(), actualGenre.isActive());
        Assertions.assertEquals(business.categories(), actualGenre.categoriesId());
        Assertions.assertEquals(business.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(business.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(business.deletedAt(), actualGenre.deletedAt());

        WireMock.verify(1, WireMock.getRequestedFor(
                WireMock.urlPathEqualTo("/api/genres/%s".formatted(business.id())))
        );
    }

    @Test
    public void givenAGenre_whenReceiveTwoCalls_shouldReturnCachedValue() {
        // given
        final var business = Fixture.Genres.business();

        final var responseBody = writeValueAsString(new GenreDto(
                business.id(),
                business.name(),
                business.active(),
                business.categories(),
                business.createdAt(),
                business.updatedAt(),
                business.deletedAt()
        ));

        final var expectedToken = "qualquerAccessToken";
        Mockito.doReturn(expectedToken)
                .when(credentialsManager)
                .retrive();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/genres/%s".formatted(business.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(responseBody)
                        )
        );

        // when
        target.genreOfId(business.id()).get();
        target.genreOfId(business.id()).get();
        final var actualGenre = target.genreOfId(business.id()).get();

        // then
        Assertions.assertEquals(business.id(), actualGenre.id());
        Assertions.assertEquals(business.name(), actualGenre.name());
        Assertions.assertEquals(business.active(), actualGenre.isActive());
        Assertions.assertEquals(business.categories(), actualGenre.categoriesId());
        Assertions.assertEquals(business.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(business.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(business.deletedAt(), actualGenre.deletedAt());

        final var actualCachedValue = cache("admin-genres").get(business.id());
        Assertions.assertEquals(actualGenre, actualCachedValue.get());

        WireMock.verify(1, WireMock.getRequestedFor(
                WireMock.urlPathEqualTo("/api/genres/%s".formatted(business.id())))
        );
    }

    // 5XX
    @Test
    public void givenAGenre_whenReceive5XXFromServer_shouldReturnInternalError() {
        // given
        final var expectedId = "123";
        final var expectedErrorStatus = 500;
        final var expecterErrorMessage = "error observed from genres [resourceId::%s] [status::%s]"
                .formatted(expectedId, expectedErrorStatus);

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error"));

        final var expectedToken = "qualquerAccessToken";
        Mockito.doReturn(expectedToken)
                .when(credentialsManager)
                .retrive();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/genres/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(expectedErrorStatus)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(responseBody)
                        )
        );

        // when
        final var actualException = Assertions.assertThrows(InternalErrorException.class,
                () -> target.genreOfId(expectedId));

        // then
        Assertions.assertEquals(expecterErrorMessage, actualException.getMessage());

        WireMock.verify(2, WireMock.getRequestedFor(
                WireMock.urlPathEqualTo("/api/genres/%s".formatted(expectedId)))
        );
    }

    // 404
    @Test
    public void givenAGenre_whenReceive404NotFoundFromServer_shouldReturnEmpty() {
        // given
        final var expectedId = "123";
        final var responseBody = writeValueAsString(Map.of("message", "not found"));

        final var expectedToken = "qualquerAccessToken";
        Mockito.doReturn(expectedToken)
                .when(credentialsManager)
                .retrive();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/genres/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(404)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(responseBody)
                        )
        );

        // when
        final var actualGenre = target.genreOfId(expectedId);

        // then
        Assertions.assertTrue(actualGenre.isEmpty());

        WireMock.verify(1, WireMock.getRequestedFor(
                WireMock.urlPathEqualTo("/api/genres/%s".formatted(expectedId)))
        );
    }

    // timeout
    @Test
    public void givenAGenre_whenReceiveTimeout_shouldReturnInternalError() {
        // given
        final var business = Fixture.Genres.business();

        final var responseBody = writeValueAsString(new GenreDto(
                business.id(),
                business.name(),
                business.active(),
                business.categories(),
                business.createdAt(),
                business.updatedAt(),
                business.deletedAt()
        ));
        final var expecterErrorMessage = "timeout observed from genres [resourceId::%s]".formatted(business.id());

        final var expectedToken = "qualquerAccessToken";
        Mockito.doReturn(expectedToken)
                .when(credentialsManager)
                .retrive();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/genres/%s".formatted(business.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withFixedDelay(1100)
                                        .withBody(responseBody)
                        )
        );

        // when
        final var actualException = Assertions.assertThrows(InternalErrorException.class,
                () -> target.genreOfId(business.id()));

        // then
        Assertions.assertEquals(expecterErrorMessage, actualException.getMessage());

        WireMock.verify(2, WireMock.getRequestedFor(
                WireMock.urlPathEqualTo("/api/genres/%s".formatted(business.id())))
        );
    }

    // bulkhead
    @Test
    public void givenAGenre_whenBulkheadIsFull_shouldReturnError() {
        // given
        final var expectedId = "123";
        final var expecterErrorMessage = "Bulkhead 'genres' is full and does not permit further calls";

        acquireBulkheadPermission(GENRE);

        // when
        final var actualException = Assertions.assertThrows(BulkheadFullException.class,
                () -> target.genreOfId(expectedId));

        // then
        // nas configuracoes de testes definimos o maximo de permissoes por chamada com o valor::1
        // ai ao da errado ja tendo uma permissao, vai da o erro especifico do bulhead
        Assertions.assertEquals(expecterErrorMessage, actualException.getMessage());

        releaseBulkheadPermission(GENRE);
    }

    @Test
    public void givenCalls_whenCBIsOpen_shouldReturnError() {
        // given
        transitionToOpenState(GENRE);
        final var expectedId = "123";
        final var expecterErrorMessage = "CircuitBreaker 'genres' is OPEN and does not permit further calls";

        // when
        final var actualException = Assertions.assertThrows(CallNotPermittedException.class, () -> target.genreOfId(expectedId));

        // then
        checkCircuitBreakerState(GENRE, CircuitBreaker.State.OPEN);
        Assertions.assertEquals(expecterErrorMessage, actualException.getMessage());

        WireMock.verify(0, WireMock.getRequestedFor(WireMock.urlPathEqualTo("/api/genres/%s".formatted(expectedId))));
    }

    @Test
    public void givenServerError_whenIsMoreThanThreshold_shouldOpenCircuitBreaker() {
        // given
        final var expectedId = "123";
        final var expectedErrorStatus = 500;
        final var expecterErrorMessage = "CircuitBreaker 'genres' is OPEN and does not permit further calls";

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error"));

        final var expectedToken = "qualquerAccessToken";
        Mockito.doReturn(expectedToken)
                .when(credentialsManager)
                .retrive();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/genres/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(expectedErrorStatus)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(responseBody)
                        )
        );

        // when
        Assertions.assertThrows(InternalErrorException.class, () -> target.genreOfId(expectedId));
        final var actualException = Assertions.assertThrows(CallNotPermittedException.class, () -> target.genreOfId(expectedId));

        // then
        checkCircuitBreakerState(GENRE, CircuitBreaker.State.OPEN);
        Assertions.assertEquals(expecterErrorMessage, actualException.getMessage());

        WireMock.verify(3, WireMock.getRequestedFor(WireMock.urlPathEqualTo("/api/genres/%s".formatted(expectedId))));
    }

}