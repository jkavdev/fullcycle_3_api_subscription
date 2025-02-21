package br.com.jkavdev.fullcycle.catalogo.infrastructure.video;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.VideoDto;

import java.util.Optional;

public interface VideoClient {

    Optional<VideoDto> videoOfId(String anId);
}
