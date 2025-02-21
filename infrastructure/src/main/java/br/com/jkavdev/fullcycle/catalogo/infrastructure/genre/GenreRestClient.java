package br.com.jkavdev.fullcycle.catalogo.infrastructure.genre;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.authentication.GetClientCredentials;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.annotations.Genres;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GenreDto;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.utils.HttpClient;
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
@CacheConfig(cacheNames = "admin-genres")
public class GenreRestClient implements GenreClient, HttpClient {

    public static final String NAMESPACE = "genres";

    private final RestClient restClient;

    private final GetClientCredentials getClientCredentials;

    public GenreRestClient(
            @Genres final RestClient genreHttpClient,
            final GetClientCredentials getClientCredentials
    ) {
        this.restClient = Objects.requireNonNull(genreHttpClient);
        this.getClientCredentials = Objects.requireNonNull(getClientCredentials);
    }

    @Override
    public String namespace() {
        return NAMESPACE;
    }

    @Cacheable(key = "#genreId")
    @Bulkhead(name = NAMESPACE)
    @CircuitBreaker(name = NAMESPACE)
    @Retry(name = NAMESPACE)
    public Optional<GenreDto> genreOfId(final String genreId) {
        final var token = getClientCredentials.retrive();
        return doGet(genreId, () -> restClient.get()
                .uri("/{id}", genreId)
                .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
                .retrieve()
                .onStatus(isNotFound, notFoundHandler(genreId))
                .onStatus(is5xx, a5xxHandler(genreId))
                .body(GenreDto.class)
        );
    }
}
