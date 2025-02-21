package br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.usecases;

import br.com.jkavdev.fullcycle.catalogo.application.genre.delete.DeleteGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.genre.get.GetAllGenreByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.genre.list.ListGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.genre.save.SaveGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration(enforceUniqueMethods = false)
public class GenreUseCasesConfig {

    private final GenreGateway genreGateway;

    public GenreUseCasesConfig(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Bean
    DeleteGenreUseCase deleteGenreUseCase() {
        return new DeleteGenreUseCase(genreGateway);
    }

    @Bean
    SaveGenreUseCase saveGenreUseCase() {
        return new SaveGenreUseCase(genreGateway);
    }

    @Bean
    ListGenreUseCase listGenreUseCase() {
        return new ListGenreUseCase(genreGateway);
    }

    @Bean
    GetAllGenreByIdUseCase getAllGenreByIdUseCase() {
        return new GetAllGenreByIdUseCase(genreGateway);
    }
}
