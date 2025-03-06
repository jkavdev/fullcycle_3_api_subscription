package br.com.jkavdev.fullcycle.subscription.infrastructure.authentication.clientcredentials;

import br.com.jkavdev.fullcycle.subscription.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JacksonTest
class KeycloakAuthenticationResultTest {

    @Autowired
    private JacksonTester<KeycloakAuthenticationGateway.KeycloakAuthenticationResult> json;

    @Test
    public void testUnmarshall() throws IOException {
        final var expectedResponse = """
                {
                    "access_token": "a351776240cf41eab9bffbcf0f28f751",
                    "refresh_token": "Documentario"
                }
                """;

        final var actualCategory = json.parse(expectedResponse);

        Assertions.assertThat(actualCategory)
                .hasFieldOrPropertyWithValue("accessToken", "a351776240cf41eab9bffbcf0f28f751")
                .hasFieldOrPropertyWithValue("refreshToken", "Documentario")
        ;

    }

}