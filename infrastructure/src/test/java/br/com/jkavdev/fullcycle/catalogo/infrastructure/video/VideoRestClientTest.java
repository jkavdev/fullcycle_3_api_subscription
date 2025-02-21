package br.com.jkavdev.fullcycle.catalogo.infrastructure.video;

import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.InternalErrorException;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.IdUtils;
import br.com.jkavdev.fullcycle.catalogo.AbstractRestClientTest;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.authentication.ClientCredentialsManager;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.ImageResourceDto;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.VideoDto;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.VideoResourceDto;
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

class VideoRestClientTest extends AbstractRestClientTest {

    @Autowired
    private VideoRestClient target;

    @SpyBean
    private ClientCredentialsManager credentialsManager;

    // OK
    @Test
    public void givenAVideo_whenReceive200FromServer_shouldBeOk() {
        // given
        final var java21 = Fixture.Videos.java21();

        final var responseBody = writeValueAsString(new VideoDto(
                java21.id(),
                java21.title(),
                java21.description(),
                java21.launchedAt().getValue(),
                java21.rating().getName(),
                java21.duration(),
                java21.opened(),
                java21.published(),
                imageResource(java21.banner()),
                imageResource(java21.thumbnail()),
                imageResource(java21.thumbnailHalf()),
                videoResource(java21.trailer()),
                videoResource(java21.video()),
                java21.categories(),
                java21.castMembers(),
                java21.genres(),
                java21.createdAt().toString(),
                java21.updatedAt().toString()
        ));

        final var expectedToken = "qualquerAccessToken";
        Mockito.doReturn(expectedToken)
                .when(credentialsManager)
                .retrive();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/videos/%s".formatted(java21.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(responseBody)
                        )
        );

        // when
        final var actualVideo = target.videoOfId(java21.id()).orElseThrow();

        // then
        Assertions.assertEquals(java21.id(), actualVideo.id());
        Assertions.assertEquals(java21.createdAt().toString(), actualVideo.createdAt());
        Assertions.assertEquals(java21.updatedAt().toString(), actualVideo.updatedAt());
        Assertions.assertEquals(java21.title(), actualVideo.title());
        Assertions.assertEquals(java21.description(), actualVideo.description());
        Assertions.assertEquals(java21.launchedAt().getValue(), actualVideo.yearLaunched());
        Assertions.assertEquals(java21.duration(), actualVideo.duration());
        Assertions.assertEquals(java21.opened(), actualVideo.opened());
        Assertions.assertEquals(java21.published(), actualVideo.published());
        Assertions.assertEquals(java21.rating().getName(), actualVideo.rating());
        Assertions.assertEquals(java21.categories(), actualVideo.categoriesId());
        Assertions.assertEquals(java21.genres(), actualVideo.genresId());
        Assertions.assertEquals(java21.castMembers(), actualVideo.castMembersId());
        Assertions.assertEquals(java21.video(), actualVideo.video().encodedLocation());
        Assertions.assertEquals(java21.trailer(), actualVideo.trailer().encodedLocation());
        Assertions.assertEquals(java21.banner(), actualVideo.banner().location());
        Assertions.assertEquals(java21.thumbnail(), actualVideo.thumbnail().location());
        Assertions.assertEquals(java21.thumbnailHalf(), actualVideo.thumbnailHalf().location());

        WireMock.verify(1, WireMock.getRequestedFor(
                WireMock.urlPathEqualTo("/api/videos/%s".formatted(java21.id())))
        );
    }

    @Test
    public void givenAVideo_whenReceiveTwoCalls_shouldReturnCachedValue() {
        // given
        final var java21 = Fixture.Videos.java21();

        final var responseBody = writeValueAsString(new VideoDto(
                java21.id(),
                java21.title(),
                java21.description(),
                java21.launchedAt().getValue(),
                java21.rating().getName(),
                java21.duration(),
                java21.opened(),
                java21.published(),
                imageResource(java21.banner()),
                imageResource(java21.thumbnail()),
                imageResource(java21.thumbnailHalf()),
                videoResource(java21.trailer()),
                videoResource(java21.video()),
                java21.categories(),
                java21.castMembers(),
                java21.genres(),
                java21.createdAt().toString(),
                java21.updatedAt().toString()
        ));

        final var expectedToken = "qualquerAccessToken";
        Mockito.doReturn(expectedToken)
                .when(credentialsManager)
                .retrive();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/videos/%s".formatted(java21.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(responseBody)
                        )
        );

        // when
        target.videoOfId(java21.id()).orElseThrow();
        target.videoOfId(java21.id()).orElseThrow();
        final var actualVideo = target.videoOfId(java21.id()).orElseThrow();

        // then
        Assertions.assertEquals(java21.id(), actualVideo.id());
        Assertions.assertEquals(java21.createdAt().toString(), actualVideo.createdAt());
        Assertions.assertEquals(java21.updatedAt().toString(), actualVideo.updatedAt());
        Assertions.assertEquals(java21.title(), actualVideo.title());
        Assertions.assertEquals(java21.description(), actualVideo.description());
        Assertions.assertEquals(java21.launchedAt().getValue(), actualVideo.yearLaunched());
        Assertions.assertEquals(java21.duration(), actualVideo.duration());
        Assertions.assertEquals(java21.opened(), actualVideo.opened());
        Assertions.assertEquals(java21.published(), actualVideo.published());
        Assertions.assertEquals(java21.rating().getName(), actualVideo.rating());
        Assertions.assertEquals(java21.categories(), actualVideo.categoriesId());
        Assertions.assertEquals(java21.genres(), actualVideo.genresId());
        Assertions.assertEquals(java21.castMembers(), actualVideo.castMembersId());
        Assertions.assertEquals(java21.video(), actualVideo.video().encodedLocation());
        Assertions.assertEquals(java21.trailer(), actualVideo.trailer().encodedLocation());
        Assertions.assertEquals(java21.banner(), actualVideo.banner().location());
        Assertions.assertEquals(java21.thumbnail(), actualVideo.thumbnail().location());
        Assertions.assertEquals(java21.thumbnailHalf(), actualVideo.thumbnailHalf().location());

        final var actualCachedValue = cache("admin-videos").get(java21.id());
        Assertions.assertEquals(actualVideo, actualCachedValue.get());

        WireMock.verify(1, WireMock.getRequestedFor(
                WireMock.urlPathEqualTo("/api/videos/%s".formatted(java21.id())))
        );
    }

    // 5XX
    @Test
    public void givenAVideo_whenReceive5XXFromServer_shouldReturnInternalError() {
        // given
        final var expectedId = "123";
        final var expectedRetries = 2;
        final var expectedErrorStatus = 500;
        final var expectedErrorMessage = "error observed from videos [resourceId::%s] [status::%s]"
                .formatted(expectedId, expectedErrorStatus);

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error"));

        final var expectedToken = "qualquerAccessToken";
        Mockito.doReturn(expectedToken)
                .when(credentialsManager)
                .retrive();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/videos/%s".formatted(expectedId)))
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
                () -> target.videoOfId(expectedId));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        WireMock.verify(expectedRetries, WireMock.getRequestedFor(
                WireMock.urlPathEqualTo("/api/videos/%s".formatted(expectedId)))
        );
    }

    // 404
    @Test
    public void givenAVideo_whenReceive404NotFoundFromServer_shouldReturnEmpty() {
        // given
        final var expectedId = "123";
        final var expectedRetries = 1;
        final var responseBody = writeValueAsString(Map.of("message", "not found"));

        final var expectedToken = "qualquerAccessToken";
        Mockito.doReturn(expectedToken)
                .when(credentialsManager)
                .retrive();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/videos/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(404)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(responseBody)
                        )
        );

        // when
        final var actualVideo = target.videoOfId(expectedId);

        // then
        Assertions.assertTrue(actualVideo.isEmpty());

        WireMock.verify(expectedRetries, WireMock.getRequestedFor(
                WireMock.urlPathEqualTo("/api/videos/%s".formatted(expectedId)))
        );
    }

    // timeout
    @Test
    public void givenAVideo_whenReceiveTimeout_shouldReturnInternalError() {
        // given
        final var java21 = Fixture.Videos.java21();

        final var responseBody = writeValueAsString(new VideoDto(
                java21.id(),
                java21.title(),
                java21.description(),
                java21.launchedAt().getValue(),
                java21.rating().getName(),
                java21.duration(),
                java21.opened(),
                java21.published(),
                imageResource(java21.banner()),
                imageResource(java21.thumbnail()),
                imageResource(java21.thumbnailHalf()),
                videoResource(java21.trailer()),
                videoResource(java21.video()),
                java21.categories(),
                java21.castMembers(),
                java21.genres(),
                java21.createdAt().toString(),
                java21.updatedAt().toString()
        ));

        final var expectedRetries = 2;
        final var expectedErrorMessage = "timeout observed from videos [resourceId::%s]".formatted(java21.id());

        final var expectedToken = "qualquerAccessToken";
        Mockito.doReturn(expectedToken)
                .when(credentialsManager)
                .retrive();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/videos/%s".formatted(java21.id())))
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
                () -> target.videoOfId(java21.id()));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        WireMock.verify(expectedRetries, WireMock.getRequestedFor(
                WireMock.urlPathEqualTo("/api/videos/%s".formatted(java21.id())))
        );
    }

    // bulkhead
    @Test
    public void givenAVideo_whenBulkheadIsFull_shouldReturnError() {
        // given
        final var expectedId = "123";
        final var expectedErrorMessage = "Bulkhead 'videos' is full and does not permit further calls";

        acquireBulkheadPermission(VIDEO);

        // when
        final var actualException = Assertions.assertThrows(BulkheadFullException.class,
                () -> target.videoOfId(expectedId));

        // then
        // nas configuracoes de testes definimos o maximo de permissoes por chamada com o valor::1
        // ai ao da errado ja tendo uma permissao, vai da o erro especifico do bulhead
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        releaseBulkheadPermission(VIDEO);
    }

    @Test
    public void givenCalls_whenCBIsOpen_shouldReturnError() {
        // given
        transitionToOpenState(VIDEO);
        final var expectedId = "123";

        final var expectedRetries = 0;
        final var expectedErrorMessage = "CircuitBreaker 'videos' is OPEN and does not permit further calls";

        // when
        final var actualException = Assertions.assertThrows(CallNotPermittedException.class, () -> target.videoOfId(expectedId));

        // then
        checkCircuitBreakerState(VIDEO, CircuitBreaker.State.OPEN);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        WireMock.verify(expectedRetries, WireMock.getRequestedFor(WireMock.urlPathEqualTo("/api/videos/%s".formatted(expectedId))));
    }

    @Test
    public void givenServerError_whenIsMoreThanThreshold_shouldOpenCircuitBreaker() {
        // given
        final var expectedId = "123";

        final var expectedRetries = 3;
        final var expectedErrorStatus = 500;
        final var expectedErrorMessage = "CircuitBreaker 'videos' is OPEN and does not permit further calls";

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error"));

        final var expectedToken = "qualquerAccessToken";
        Mockito.doReturn(expectedToken)
                .when(credentialsManager)
                .retrive();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/videos/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(expectedErrorStatus)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(responseBody)
                        )
        );

        // when
        Assertions.assertThrows(InternalErrorException.class, () -> target.videoOfId(expectedId));
        final var actualException = Assertions.assertThrows(CallNotPermittedException.class, () -> target.videoOfId(expectedId));

        // then
        checkCircuitBreakerState(VIDEO, CircuitBreaker.State.OPEN);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        WireMock.verify(expectedRetries, WireMock.getRequestedFor(WireMock.urlPathEqualTo("/api/videos/%s".formatted(expectedId))));
    }

    private static VideoResourceDto videoResource(final String data) {
        return new VideoResourceDto(IdUtils.uniqueId(), IdUtils.uniqueId(), data, data, data, "processed");
    }

    private static ImageResourceDto imageResource(final String data) {
        return new ImageResourceDto(IdUtils.uniqueId(), IdUtils.uniqueId(), data, data);
    }

}