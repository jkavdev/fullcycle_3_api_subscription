package br.com.jkavdev.fullcycle.catalogo.infrastructure.genre;

import br.com.jkavdev.fullcycle.catalogo.domain.genre.Genre;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("development")
public class GenreInMemoryGateway implements GenreGateway {

    private final ConcurrentHashMap<String, Genre> db;

    public GenreInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public Genre save(Genre aGenre) {
        this.db.put(aGenre.id(), aGenre);
        return aGenre;
    }

    @Override
    public void deleteById(String anId) {
        db.remove(anId);
    }

    @Override
    public Optional<Genre> findById(String anId) {
        return Optional.ofNullable(db.get(anId));
    }

    @Override
    public List<Genre> findAllById(Set<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return ids.stream()
                .map(db::get)
                .toList();
    }

    @Override
    public Pagination<Genre> findAll(GenreSearchQuery aQuery) {
        final var values = db.values();
        return new Pagination<>(aQuery.page(), aQuery.perPage(), values.size(), new ArrayList<>(values));
    }

}
