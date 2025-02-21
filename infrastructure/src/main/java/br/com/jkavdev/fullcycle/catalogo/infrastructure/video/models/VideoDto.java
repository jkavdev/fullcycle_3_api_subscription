package br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models;

import java.util.Optional;
import java.util.Set;

public record VideoDto(
        String id,
        String title,
        String description,
        int yearLaunched,
        String rating,
        Double duration,
        boolean opened,
        boolean published,
        ImageResourceDto banner,
        ImageResourceDto thumbnail,
        ImageResourceDto thumbnailHalf,
        VideoResourceDto trailer,
        VideoResourceDto video,
        Set<String> categoriesId,
        Set<String> castMembersId,
        Set<String> genresId,
        String createdAt,
        String updatedAt
) {

    public Optional<VideoResourceDto> getVideo() {
        return Optional.ofNullable(video);
    }

    public Optional<VideoResourceDto> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<ImageResourceDto> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<ImageResourceDto> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<ImageResourceDto> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }

}
