package br.com.jkavdev.fullcycle.subscription;

import br.com.jkavdev.fullcycle.subscription.infrastructure.configuration.WebServerConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-integration")
@AutoConfigureWireMock(port = 0)
@SpringBootTest(
        classes = {
                WebServerConfig.class,
                IntegrationTestConfiguration.class
        }
)
@Tag("integrationTests")
public abstract class AbstractRestClientTest {

    @Autowired
    private ObjectMapper objectMapper;

    // evitando que o contexto de um teste interfira em outro teste, ai limpamos tudo
    @BeforeEach
    void beforeEach() {
        WireMock.reset();
        WireMock.resetAllRequests();
    }

    protected String writeValueAsString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
