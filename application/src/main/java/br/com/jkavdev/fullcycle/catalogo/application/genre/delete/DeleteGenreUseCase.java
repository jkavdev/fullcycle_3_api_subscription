package br.com.jkavdev.fullcycle.catalogo.application.genre.delete;

import br.com.jkavdev.fullcycle.catalogo.application.UnitUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreGateway;

import java.util.Objects;

public class DeleteGenreUseCase extends UnitUseCase<DeleteGenreUseCase.Input> {

    private final GenreGateway genreGateway;

    public DeleteGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(final Input input) {
        if (input == null || input.id() == null) {
            return;
        }

        genreGateway.deleteById(input.id());
    }

    public record Input(String id) {

    }
}
