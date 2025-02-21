package br.com.jkavdev.fullcycle.catalogo.infrastructure.video;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.authentication.GetClientCredentials;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.annotations.Videos;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.utils.HttpClient;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.VideoDto;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.Optional;

@Component
@CacheConfig(cacheNames = "admin-videos")
public class VideoRestClient implements VideoClient, HttpClient {

    public static final String NAMESPACE = "videos";

    private final RestClient restClient;

    private final GetClientCredentials getClientCredentials;

    public VideoRestClient(
            @Videos final RestClient videoHttpClient,
            final GetClientCredentials getClientCredentials
    ) {
        this.restClient = Objects.requireNonNull(videoHttpClient);
        this.getClientCredentials = Objects.requireNonNull(getClientCredentials);
    }

    @Override
    public String namespace() {
        return NAMESPACE;
    }

    @Cacheable(key = "#videoId")
    @Bulkhead(name = NAMESPACE)
    @CircuitBreaker(name = NAMESPACE)
    @Retry(name = NAMESPACE)
    public Optional<VideoDto> videoOfId(final String videoId) {
        final var token = getClientCredentials.retrive();
        return doGet(videoId, () -> restClient.get()
                .uri("/{id}", videoId)
                .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
                .retrieve()
                .onStatus(isNotFound, notFoundHandler(videoId))
                .onStatus(is5xx, a5xxHandler(videoId))
                .body(VideoDto.class)
        );
    }
}
