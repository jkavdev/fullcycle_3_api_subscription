package br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private String bootstrapServers;

    private int poolTimeout;

    private boolean autoCreateTopics;

    public String bootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public int poolTimeout() {
        return poolTimeout;
    }

    public void setPoolTimeout(int poolTimeout) {
        this.poolTimeout = poolTimeout;
    }

    public boolean autoCreateTopics() {
        return autoCreateTopics;
    }

    public void setAutoCreateTopics(boolean autoCreateTopics) {
        this.autoCreateTopics = autoCreateTopics;
    }

    @Override
    public String toString() {
        return "KafkaProperties{" +
                "bootstrapServers='" + bootstrapServers + '\'' +
                ", poolTimeout=" + poolTimeout +
                ", autoCreateTopics=" + autoCreateTopics +
                '}';
    }
}
