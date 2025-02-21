package br.com.jkavdev.fullcycle.subscription.infrastructure.authentication;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.InternalErrorException;
import br.com.jkavdev.fullcycle.subscription.infrastructure.configuration.annotations.Keycloak;
import br.com.jkavdev.fullcycle.subscription.infrastructure.configuration.properties.KeycloakProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Component
public class KeycloakAuthenticationGateway implements AuthenticationGateway {

    private final RestClient restClient;

    private final String tokenUri;

    public KeycloakAuthenticationGateway(
            @Keycloak final RestClient restClient,
            final KeycloakProperties properties
    ) {
        this.restClient = Objects.requireNonNull(restClient);
        this.tokenUri = properties.tokenUri();
    }

    @Override
    public AuthenticationResult login(final ClientCredentialsInput input) {
        final var map = new LinkedMultiValueMap<>();
        map.set("grant_type", "client_credentials");
        map.set("client_id", input.clientId());
        map.set("client_secret", input.clientSecret());

        final var output = restClient.post()
                .uri(tokenUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(map)
                .retrieve()
                .body(KeycloakAuthenticationResult.class);

        if (output == null) {
            throw InternalErrorException.with("failed to create client credentials [clientId::%s]".formatted(input.clientId()));
        }

        return new AuthenticationResult(output.accessToken, output.refreshToken);
    }

    @Override
    public AuthenticationResult refresh(final RefreshTokenInput input) {
        final var map = new LinkedMultiValueMap<>();
        map.set("grant_type", "refresh_token");
        map.set("client_id", input.clientId());
        map.set("client_secret", input.clientSecret());
        map.set("refresh_token", input.refreshToken());

        final var output = restClient.post()
                .uri(tokenUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(map)
                .retrieve()
                .body(KeycloakAuthenticationResult.class);

        if (output == null) {
            throw InternalErrorException.with("failed to refresh client credentials [clientId::%s]".formatted(input.clientId()));
        }

        return new AuthenticationResult(output.accessToken, output.refreshToken);
    }

    public record KeycloakAuthenticationResult(
            String accessToken,
            String refreshToken
    ) {

    }

}
