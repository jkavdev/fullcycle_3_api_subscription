package br.com.jkavdev.fullcycle.subscription.infrastructure.configuration;

import br.com.jkavdev.fullcycle.subscription.infrastructure.jdbc.DatabaseClient;
import br.com.jkavdev.fullcycle.subscription.infrastructure.jdbc.JdbcClientAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration(proxyBeanMethods = false)
public class JdbcConfig {

    @Bean
    DatabaseClient databaseClient(final JdbcClient jdbcClient) {
        return new JdbcClientAdapter(jdbcClient);
    }

}
