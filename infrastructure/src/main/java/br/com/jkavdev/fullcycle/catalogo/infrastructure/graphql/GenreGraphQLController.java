package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.application.genre.list.ListGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.genre.save.SaveGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.security.Roles;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.GqlGenrePresenter;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GqlGenre;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GqlGenreInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class GenreGraphQLController {

    private final ListGenreUseCase listGenreUseCase;

    private final SaveGenreUseCase saveGenreUseCase;

    public GenreGraphQLController(
            final ListGenreUseCase listGenreUseCase,
            final SaveGenreUseCase saveGenreUseCase
    ) {
        this.listGenreUseCase = Objects.requireNonNull(listGenreUseCase);
        this.saveGenreUseCase = Objects.requireNonNull(saveGenreUseCase);
    }

    @QueryMapping
    @Secured({Roles.ROLE_ADMIN, Roles.ROLE_SUBSCRIBER, Roles.ROLE_GENRES})
    public List<GqlGenre> genres(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction,
            @Argument final Set<String> categories
    ) {
        final var input =
                new ListGenreUseCase.Input(page, perPage, search, sort, direction, categories);

        return listGenreUseCase.execute(input)
                .map(GqlGenrePresenter::present)
                .data();
    }

    @MutationMapping
    @Secured({Roles.ROLE_ADMIN, Roles.ROLE_SUBSCRIBER, Roles.ROLE_GENRES})
    public SaveGenreUseCase.Output saveGenre(@Argument(name = "input") final GqlGenreInput genreInput) {
        final var input = new SaveGenreUseCase.Input(
                genreInput.id(), genreInput.name(), genreInput.active(), genreInput.categories(), genreInput.createdAt(), genreInput.updatedAt(), genreInput.deletedAt()
        );
        return saveGenreUseCase.execute(input);
    }

}
