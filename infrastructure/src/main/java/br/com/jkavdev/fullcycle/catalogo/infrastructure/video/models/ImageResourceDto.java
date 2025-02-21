package br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models;

public record ImageResourceDto(
        String id,
        String name,
        String checksum,
        String location
) {
}
