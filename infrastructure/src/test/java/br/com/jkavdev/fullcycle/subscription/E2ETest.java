package br.com.jkavdev.fullcycle.subscription;

import br.com.jkavdev.fullcycle.subscription.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-e2e")
@SpringBootTest(classes = WebServerConfig.class)
@ExtendWith(TimeZoneSetup.class)
@AutoConfigureMockMvc
@Tag("e2eTests")
public @interface E2ETest {

}