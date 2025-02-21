package br.com.jkavdev.fullcycle.catalogo.domain.video;

import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {

    Video save(Video aVideo);

    void deleteById(String videoId);

    Optional<Video> findById(String videoId);

    Pagination<Video> findAll(VideoSearchQuery aQuery);

}
