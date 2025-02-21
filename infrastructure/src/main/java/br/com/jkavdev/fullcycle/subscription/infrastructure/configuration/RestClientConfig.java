package br.com.jkavdev.fullcycle.subscription.infrastructure.configuration;

import br.com.jkavdev.fullcycle.subscription.infrastructure.configuration.annotations.Keycloak;
import br.com.jkavdev.fullcycle.subscription.infrastructure.configuration.properties.RestClientProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class RestClientConfig {

    @Bean
    @Keycloak
    public RestClient keycloakHttpClient(
            @Keycloak final RestClientProperties properties,
            final ObjectMapper objectMapper
    ) {
        return restClient(properties, objectMapper);
    }

    private static RestClient restClient(
            final RestClientProperties properties,
            final ObjectMapper objectMapper
    ) {
        final var factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(properties.readTimeout());

        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .requestFactory(factory)
                .messageConverters(converters -> {
                    converters.removeIf(it -> it instanceof MappingJackson2HttpMessageConverter);
                    converters.add(jsonConverter(objectMapper));
                    converters.add(new FormHttpMessageConverter());
                })
                .build();
    }

    private static MappingJackson2HttpMessageConverter jsonConverter(final ObjectMapper objectMapper) {
        final var jsonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        jsonConverter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON));
        return jsonConverter;
    }

}
