package br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models;

public record VideoResourceDto(
        String id,
        String checksum,
        String name,
        String location,
        String encodedLocation,
        String status
) {
}
