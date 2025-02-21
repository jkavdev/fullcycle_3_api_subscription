package br.com.jkavdev.fullcycle.catalogo.infrastructure.authentication;

import br.com.jkavdev.fullcycle.catalogo.AbstractRestClientTest;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.json.Json;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class KeycloakAuthenticationGatewayTest extends AbstractRestClientTest {

    @Autowired
    private KeycloakAuthenticationGateway gateway;

    @Test
    public void givenValidParams_whenCallsLogin_shoulReturnCLientCredentials() {
        // given
        final var expectedClientId = "qualquerClientId";
        final var expectedClientSecret = "qualquerSecret";
        final var expectedAccessToken = "qualquerAccessToken";
        final var expectedRefreshToken = "qualquerRefreshToken";

        WireMock.stubFor(
                WireMock.post(WireMock.urlPathEqualTo("/realms/test/protocol/openid-connect/token"))
                        .withHeader(HttpHeaders.ACCEPT, WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                        .withHeader(HttpHeaders.CONTENT_TYPE, WireMock.equalTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(
                                        Json.writeValueAsString(new KeycloakAuthenticationGateway.KeycloakAuthenticationResult(
                                                expectedAccessToken, expectedRefreshToken
                                        ))
                                ))
        );

        // when
        final var actualOutput =
                gateway.login(new AuthenticationGateway.ClientCredentialsInput(expectedClientId, expectedClientSecret));

        // then
        Assertions.assertEquals(expectedAccessToken, actualOutput.accessToken());
        Assertions.assertEquals(expectedRefreshToken, actualOutput.refreshToken());
    }

    @Test
    public void givenValidParams_whenCallsRefresh_shoulReturnCLientCredentials() {
        // given
        final var expectedClientId = "qualquerClientId";
        final var expectedClientSecret = "qualquerSecret";
        final var expectedAccessToken = "qualquerAccessToken2";
        final var expectedRefreshToken = "qualquerRefreshToken2";

        WireMock.stubFor(
                WireMock.post(WireMock.urlPathEqualTo("/realms/test/protocol/openid-connect/token"))
                        .withHeader(HttpHeaders.ACCEPT, WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                        .withHeader(HttpHeaders.CONTENT_TYPE, WireMock.equalTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(
                                        Json.writeValueAsString(new KeycloakAuthenticationGateway.KeycloakAuthenticationResult(
                                                expectedAccessToken, expectedRefreshToken
                                        ))
                                ))
        );

        // when
        final var actualOutput =
                gateway.refresh(new AuthenticationGateway.RefreshTokenInput(expectedClientId, expectedClientSecret, "refresh"));

        // then
        Assertions.assertEquals(expectedAccessToken, actualOutput.accessToken());
        Assertions.assertEquals(expectedRefreshToken, actualOutput.refreshToken());
    }

}