package br.com.jkavdev.fullcycle.catalogo.application.genre.save;

import br.com.jkavdev.fullcycle.catalogo.application.UseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.Genre;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.validation.Error;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public class SaveGenreUseCase extends UseCase<SaveGenreUseCase.Input, SaveGenreUseCase.Output> {

    private final GenreGateway genreGateway;

    public SaveGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Output execute(final Input input) {
        if (input == null) {
            throw DomainException.with(new Error("'SaveGenreUseCase.Input' cannot be null"));
        }
        final var aGenre = Genre.with(
                input.id(),
                input.name(),
                input.active(),
                input.categories(),
                input.createdAt(),
                input.updatedAt(),
                input.deletedAt()
        );

        genreGateway.save(aGenre);

        return new Output(aGenre.id());
    }

    public record Input(
            String id,
            String name,
            boolean active,
            Set<String> categories,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt
    ) {

    }

    public record Output(String id) {

    }
}
