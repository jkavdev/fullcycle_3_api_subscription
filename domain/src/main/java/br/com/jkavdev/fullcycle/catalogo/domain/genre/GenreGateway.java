package br.com.jkavdev.fullcycle.catalogo.domain.genre;

import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreGateway {

    Genre save(Genre aGenre);

    void deleteById(String genreId);

    Optional<Genre> findById(String genreId);

    List<Genre> findAllById(Set<String> ids);

    Pagination<Genre> findAll(GenreSearchQuery aQuery);

}
