package br.com.jkavdev.fullcycle.catalogo.infrastructure.authentication;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.properties.KeycloakProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

@Component
public class ClientCredentialsManager implements GetClientCredentials, RefreshClientCredentials {

    private static final AtomicReferenceFieldUpdater<ClientCredentialsManager, ClientCredentials> UPDATER
            = AtomicReferenceFieldUpdater.newUpdater(ClientCredentialsManager.class, ClientCredentials.class, "credentials");

    private volatile ClientCredentials credentials;

    private final AuthenticationGateway authenticationGateway;

    private final KeycloakProperties keycloakProperties;

    public ClientCredentialsManager(
            final AuthenticationGateway authenticationGateway,
            final KeycloakProperties keycloakProperties
    ) {
        this.authenticationGateway = Objects.requireNonNull(authenticationGateway);
        this.keycloakProperties = Objects.requireNonNull(keycloakProperties);
    }

    @Override
    public String retrive() {
        return credentials.accessToken;
    }

    @Override
    public void refresh() {
        final var result = credentials == null
                ? login()
                : refreshToken();

        UPDATER.set(this, new ClientCredentials(clientId(), result.accessToken(), result.refreshToken()));
    }

    private AuthenticationGateway.AuthenticationResult login() {
        return authenticationGateway.login(new AuthenticationGateway.ClientCredentialsInput(clientId(), clientSecret()));
    }

    private AuthenticationGateway.AuthenticationResult refreshToken() {
        try {
            return authenticationGateway.refresh(new AuthenticationGateway.RefreshTokenInput(clientId(), clientSecret(), credentials.refreshToken()));
        } catch (RuntimeException e) {
            return login();
        }
    }

    private String clientId() {
        return keycloakProperties.clientId();
    }

    private String clientSecret() {
        return keycloakProperties.clientSecret();
    }

    record ClientCredentials(String clientId, String accessToken, String refreshToken) {

    }
}
