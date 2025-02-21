package br.com.jkavdev.fullcycle.subscription.infrastructure.configuration;

import br.com.jkavdev.fullcycle.subscription.infrastructure.json.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;

@JsonComponent
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return Json.mapper();
    }
}
