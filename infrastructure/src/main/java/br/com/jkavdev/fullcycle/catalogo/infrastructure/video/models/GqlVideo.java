package br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models;

import java.util.Set;

public record GqlVideo(
        String id,
        String title,
        String description,
        int yearLaunched,
        String rating,
        Double duration,
        boolean opened,
        boolean published,
        String banner,
        String thumbnail,
        String thumbnailHalf,
        String trailer,
        String video,
        Set<String> categoriesId,
        Set<String> castMembersId,
        Set<String> genresId,
        String createdAt,
        String updatedAt
) {

}
