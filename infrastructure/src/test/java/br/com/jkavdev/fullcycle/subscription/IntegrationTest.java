package br.com.jkavdev.fullcycle.subscription;

import br.com.jkavdev.fullcycle.subscription.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

// indicando que sera usada nas classes de testes
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
// definindo profile de teste
@ActiveProfiles("test-integration")
@EnableAutoConfiguration(
        exclude = ElasticsearchRepositoriesAutoConfiguration.class
)
// para testes integrados precisamos do spring com todo o seu contexto
@ExtendWith(TimeZoneSetup.class)
@SpringBootTest(classes = {
        WebServerConfig.class,
        IntegrationTestConfiguration.class
})
@Tag("integrationTests")
public @interface IntegrationTest {

}