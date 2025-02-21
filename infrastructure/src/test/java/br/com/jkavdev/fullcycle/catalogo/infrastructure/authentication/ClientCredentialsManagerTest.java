package br.com.jkavdev.fullcycle.catalogo.infrastructure.authentication;

import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.InternalErrorException;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.properties.KeycloakProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ClientCredentialsManagerTest {

    @Mock
    private KeycloakProperties keycloakProperties;

    @Mock
    private AuthenticationGateway authenticationGateway;

    @InjectMocks
    private ClientCredentialsManager manager;

    @Test
    public void givenValidAuthenticationResult_whenCallsRefresh_shouldCreateCredentials() {
        // given
        final var expectedAccessToken = "qualquerAccessToken";
        final var expectedRefreshToken = "qualquerRefreshToken";
        final var expectedClientId = "qualquerClientId";
        final var expectedClientSecret = "qualquerClientSecret";

        Mockito.doReturn(expectedClientId)
                .when(keycloakProperties)
                .clientId();
        Mockito.doReturn(expectedClientSecret)
                .when(keycloakProperties)
                .clientSecret();

        Mockito.doReturn(new AuthenticationGateway.AuthenticationResult(expectedAccessToken, expectedRefreshToken))
                .when(authenticationGateway)
                .login(new AuthenticationGateway.ClientCredentialsInput(expectedClientId, expectedClientSecret));

        // when
        manager.refresh();
        final var actualToken = manager.retrive();

        // then
        Assertions.assertEquals(expectedAccessToken, actualToken);
    }

    @Test
    public void givenPreviousAuthenticationResult_whenCallsRefresh_shouldUpdateCredentials() {
        // given
        final var expectedAccessToken = "qualquerAccessToken";
        final var expectedRefreshToken = "qualquerRefreshToken";
        final var expectedClientId = "qualquerClientId";
        final var expectedClientSecret = "qualquerClientSecret";

        ReflectionTestUtils.setField(manager, "credentials",
                new ClientCredentialsManager.ClientCredentials(expectedClientId, "acc", "ref"));

        Mockito.doReturn(expectedClientId)
                .when(keycloakProperties)
                .clientId();
        Mockito.doReturn(expectedClientSecret)
                .when(keycloakProperties)
                .clientSecret();

        Mockito.doReturn(new AuthenticationGateway.AuthenticationResult(expectedAccessToken, expectedRefreshToken))
                .when(authenticationGateway)
                .refresh(new AuthenticationGateway.RefreshTokenInput(expectedClientId, expectedClientSecret, "ref"));

        // when
        manager.refresh();

        final var actualCredentials =
                (ClientCredentialsManager.ClientCredentials) ReflectionTestUtils.getField(manager, "credentials");

        // then
        Assertions.assertEquals(expectedAccessToken, actualCredentials.accessToken());
        Assertions.assertEquals(expectedRefreshToken, actualCredentials.refreshToken());
    }

    @Test
    public void givenErrorFromRefresh_whenCallsRefresh_shouldFallbackToLogin() {
        // given
        final var expectedAccessToken = "qualquerAccessToken";
        final var expectedRefreshToken = "qualquerRefreshToken";
        final var expectedClientId = "qualquerClientId";
        final var expectedClientSecret = "qualquerClientSecret";

        ReflectionTestUtils.setField(manager, "credentials",
                new ClientCredentialsManager.ClientCredentials(expectedClientId, "acc", "ref"));

        Mockito.doReturn(expectedClientId)
                .when(keycloakProperties)
                .clientId();
        Mockito.doReturn(expectedClientSecret)
                .when(keycloakProperties)
                .clientSecret();

        Mockito.doThrow(InternalErrorException.with("deu ruim,......................"))
                .when(authenticationGateway)
                .refresh(new AuthenticationGateway.RefreshTokenInput(expectedClientId, expectedClientSecret, "ref"));

        Mockito.doReturn(new AuthenticationGateway.AuthenticationResult(expectedAccessToken, expectedRefreshToken))
                .when(authenticationGateway)
                .login(new AuthenticationGateway.ClientCredentialsInput(expectedClientId, expectedClientSecret));

        // when
        manager.refresh();

        final var actualCredentials =
                (ClientCredentialsManager.ClientCredentials) ReflectionTestUtils.getField(manager, "credentials");

        // then
        Assertions.assertEquals(expectedAccessToken, actualCredentials.accessToken());
        Assertions.assertEquals(expectedRefreshToken, actualCredentials.refreshToken());
    }

}