package br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models;

import br.com.jkavdev.fullcycle.catalogo.domain.genre.Genre;

import java.time.Instant;
import java.util.Set;

public record GenreDto(
        String id,
        String name,
        Boolean isActive,
        Set<String> categoriesId,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static GenreDto from(final Genre genre) {
        return new GenreDto(
                genre.id(),
                genre.name(),
                genre.active(),
                genre.categories(),
                genre.createdAt(),
                genre.updatedAt(),
                genre.deletedAt()
        );
    }

    public Boolean isActive() {
        return isActive != null ? isActive : true;
    }

    public Genre toGenre() {
        return Genre.with(id(), name(), isActive(), categoriesId(), createdAt(), updatedAt(), deletedAt());
    }
}