package br.com.jkavdev.fullcycle.catalogo.infrastructure.genre;

import br.com.jkavdev.fullcycle.catalogo.application.genre.get.GetAllGenreByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.genre.list.ListGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GqlGenre;

import java.time.Instant;

public final class GqlGenrePresenter {

    private GqlGenrePresenter() {

    }

    public static GqlGenre present(final ListGenreUseCase.Output out) {
        return new GqlGenre(
                out.id(),
                out.name(),
                out.active(),
                out.categories(),
                formatDate(out.createdAt()),
                formatDate(out.updatedAt()),
                formatDate(out.deletedAt())
        );
    }

    public static GqlGenre present(final GetAllGenreByIdUseCase.Output out) {
        return new GqlGenre(
                out.id(),
                out.name(),
                out.active(),
                out.categories(),
                formatDate(out.createdAt()),
                formatDate(out.updatedAt()),
                formatDate(out.deletedAt())
        );
    }

    private static String formatDate(final Instant date) {
        return date != null ? date.toString() : "";
    }

}
