package br.com.jkavdev.fullcycle.catalogo.infrastructure.video;

import br.com.jkavdev.fullcycle.catalogo.application.video.list.ListVideoUseCase;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.GqlVideo;

import java.time.Instant;

public final class GqlVideoPresenter {

    private GqlVideoPresenter() {

    }

    public static GqlVideo present(final ListVideoUseCase.Output out) {
        return new GqlVideo(
                out.id(),
                out.title(),
                out.description(),
                out.yearLaunched(),
                out.rating(),
                out.duration(),
                out.opened(),
                out.published(),
                out.banner(),
                out.thumbnail(),
                out.thumbnailHalf(),
                out.trailer(),
                out.video(),
                out.categoriesId(),
                out.castMembersId(),
                out.genresId(),
                formatDate(out.createdAt()),
                formatDate(out.updatedAt())
        );
    }

    private static String formatDate(final Instant date) {
        return date != null ? date.toString() : "";
    }

}
