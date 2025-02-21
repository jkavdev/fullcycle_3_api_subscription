package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.authentication.GetClientCredentials;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models.CategoryDto;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.annotations.Categories;
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
@CacheConfig(cacheNames = "admin-categories")
public class CategoryRestClient implements CategoryClient, HttpClient {

    public static final String NAMESPACE = "categories";

    private final RestClient restClient;

    private final GetClientCredentials getClientCredentials;

    public CategoryRestClient(
            @Categories final RestClient categoryHttpClient,
            final GetClientCredentials getClientCredentials
    ) {
        this.restClient = Objects.requireNonNull(categoryHttpClient);
        this.getClientCredentials = Objects.requireNonNull(getClientCredentials);
    }

    @Override
    public String namespace() {
        return NAMESPACE;
    }

    @Cacheable(key = "#categoryId")
    @Bulkhead(name = NAMESPACE)
    @CircuitBreaker(name = NAMESPACE)
    @Retry(name = NAMESPACE)
    public Optional<Category> categoryOfId(final String categoryId) {
        final var token = getClientCredentials.retrive();
        return doGet(categoryId, () -> restClient.get()
                .uri("/{id}", categoryId)
                .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
                .retrieve()
                .onStatus(isNotFound, notFoundHandler(categoryId))
                .onStatus(is5xx, a5xxHandler(categoryId))
                .body(CategoryDto.class)
        ).map(CategoryDto::toCategory);
    }
}
