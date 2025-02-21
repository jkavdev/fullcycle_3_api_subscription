package br.com.jkavdev.fullcycle.catalogo.infrastructure.genre;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GenreDto;

import java.util.Optional;

public interface GenreClient {

    Optional<GenreDto> genreOfId(String anId);
}
