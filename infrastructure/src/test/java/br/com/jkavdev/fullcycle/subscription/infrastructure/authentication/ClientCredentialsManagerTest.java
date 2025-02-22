package br.com.jkavdev.fullcycle.subscription.infrastructure.authentication;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.InternalErrorException;
import br.com.jkavdev.fullcycle.subscription.infrastructure.configuration.properties.KeycloakProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

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
        final var expectedAccessToken = "access";
        final var expectedRefreshToken = "refresh";
        final var expectedClientId = "client-id";
        final var expectedClientSecret = "sad1324213";

        doReturn(expectedClientId).when(keycloakProperties).clientId();
        doReturn(expectedClientSecret).when(keycloakProperties).clientSecret();

        doReturn(new AuthenticationGateway.AuthenticationResult(expectedAccessToken, expectedRefreshToken))
                .when(authenticationGateway).login(new AuthenticationGateway.ClientCredentialsInput(expectedClientId, expectedClientSecret));

        // when
        this.manager.refresh();
        final var actualToken = this.manager.retrieve();

        // then
        Assertions.assertEquals(expectedAccessToken, actualToken);
    }

    @Test
    public void givenPreviousAuthentication_whenCallsRefresh_shouldUpdateCredentials() {
        // given
        final var expectedAccessToken = "access";
        final var expectedRefreshToken = "refresh";
        final var expectedClientId = "client-id";
        final var expectedClientSecret = "sad1324213";

        ReflectionTestUtils.setField(this.manager, "credentials", new ClientCredentialsManager.ClientCredentials(expectedClientId, "acc", "ref"));

        doReturn(expectedClientId).when(keycloakProperties).clientId();
        doReturn(expectedClientSecret).when(keycloakProperties).clientSecret();

        doReturn(new AuthenticationGateway.AuthenticationResult(expectedAccessToken, expectedRefreshToken))
                .when(authenticationGateway).refresh(new AuthenticationGateway.RefreshTokenInput(expectedClientId, expectedClientSecret, "ref"));

        // when
        this.manager.refresh();

        final var actualCredentials = (ClientCredentialsManager.ClientCredentials) ReflectionTestUtils.getField(this.manager, "credentials");

        // then
        Assertions.assertEquals(expectedAccessToken, actualCredentials.accessToken());
        Assertions.assertEquals(expectedRefreshToken, actualCredentials.refreshToken());
    }

    @Test
    public void givenErrorFromRefreshToken_whenCallsRefresh_shouldFallbackToLogin() {
        // given
        final var expectedAccessToken = "access";
        final var expectedRefreshToken = "refresh";
        final var expectedClientId = "client-id";
        final var expectedClientSecret = "sad1324213";

        ReflectionTestUtils.setField(this.manager, "credentials", new ClientCredentialsManager.ClientCredentials(expectedClientId, "acc", "ref"));

        doReturn(expectedClientId).when(keycloakProperties).clientId();
        doReturn(expectedClientSecret).when(keycloakProperties).clientSecret();

        doThrow(InternalErrorException.with("BLA!"))
                .when(authenticationGateway).refresh(new AuthenticationGateway.RefreshTokenInput(expectedClientId, expectedClientSecret, "ref"));

        doReturn(new AuthenticationGateway.AuthenticationResult(expectedAccessToken, expectedRefreshToken))
                .when(authenticationGateway).login(new AuthenticationGateway.ClientCredentialsInput(expectedClientId, expectedClientSecret));

        // when
        this.manager.refresh();

        final var actualCredentials = (ClientCredentialsManager.ClientCredentials) ReflectionTestUtils.getField(this.manager, "credentials");

        // then
        Assertions.assertEquals(expectedAccessToken, actualCredentials.accessToken());
        Assertions.assertEquals(expectedRefreshToken, actualCredentials.refreshToken());
    }
}