package br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.usecases;

import br.com.jkavdev.fullcycle.catalogo.application.video.delete.DeleteVideoUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.video.get.GetVideoUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.video.list.ListVideoUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.video.save.SaveVideoUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration(proxyBeanMethods = false)
public class VideoUseCasesConfig {

    private final VideoGateway videoGateway;

    public VideoUseCasesConfig(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Bean
    DeleteVideoUseCase deleteVideoUseCase() {
        return new DeleteVideoUseCase(videoGateway);
    }

    @Bean
    ListVideoUseCase listVideoUseCase() {
        return new ListVideoUseCase(videoGateway);
    }

    @Bean
    SaveVideoUseCase saveVideoUseCase() {
        return new SaveVideoUseCase(videoGateway);
    }

    // TODO: porque eu nao defini esse bean?
    @Bean
    GetVideoUseCase getVideoUseCase() {
        return new GetVideoUseCase(videoGateway);
    }
}
