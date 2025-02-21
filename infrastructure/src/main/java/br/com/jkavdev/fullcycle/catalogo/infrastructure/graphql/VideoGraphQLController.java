package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.application.castmember.get.GetAllCastMemberByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.get.GetAllCategoryByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.genre.get.GetAllGenreByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.video.list.ListVideoUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.video.save.SaveVideoUseCase;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.GqlCastMemberPresenter;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.models.GqlCastMember;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.GqlCategoryPresenter;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models.GqlCategory;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.security.Roles;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.GqlGenrePresenter;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GqlGenre;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.GqlVideoPresenter;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.GqlVideo;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.GqlVideoInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class VideoGraphQLController {

    private final ListVideoUseCase listVideoUseCase;

    private final GetAllCastMemberByIdUseCase getAllCastMemberByIdUseCase;

    private final GetAllCategoryByIdUseCase getAllCategoryByIdUseCase;

    private final GetAllGenreByIdUseCase getAllGenreByIdUseCase;

    private final SaveVideoUseCase saveVideoUseCase;

    public VideoGraphQLController(
            final ListVideoUseCase listVideoUseCase,
            final GetAllCastMemberByIdUseCase getAllCastMemberByIdUseCase,
            final GetAllCategoryByIdUseCase getAllCategoryByIdUseCase,
            final GetAllGenreByIdUseCase getAllGenreByIdUseCase,
            final SaveVideoUseCase saveVideoUseCase
    ) {
        this.listVideoUseCase = Objects.requireNonNull(listVideoUseCase);
        this.getAllCastMemberByIdUseCase = Objects.requireNonNull(getAllCastMemberByIdUseCase);
        this.getAllCategoryByIdUseCase = Objects.requireNonNull(getAllCategoryByIdUseCase);
        this.getAllGenreByIdUseCase = Objects.requireNonNull(getAllGenreByIdUseCase);
        this.saveVideoUseCase = Objects.requireNonNull(saveVideoUseCase);
    }

    @QueryMapping
    @Secured({Roles.ROLE_ADMIN, Roles.ROLE_SUBSCRIBER, Roles.ROLE_VIDEOS})
    public List<GqlVideo> videos(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction,
            @Argument final String rating,
            @Argument final Integer yearLaunched,
            @Argument final Set<String> castMembers,
            @Argument final Set<String> categories,
            @Argument final Set<String> genres
    ) {
        final var input =
                new ListVideoUseCase.Input(
                        page, perPage, search, sort, direction, rating, yearLaunched, castMembers, categories, genres
                );

        return listVideoUseCase.execute(input)
                .map(GqlVideoPresenter::present)
                .data();
    }

    @SchemaMapping(typeName = "Video", field = "castMembers")
    @Secured({Roles.ROLE_ADMIN, Roles.ROLE_SUBSCRIBER, Roles.ROLE_VIDEOS})
    public List<GqlCastMember> castMembers(final GqlVideo video) {
        return getAllCastMemberByIdUseCase.execute(
                        new GetAllCastMemberByIdUseCase.Input(video.castMembersId())
                ).stream()
                .map(GqlCastMemberPresenter::present)
                .toList();
    }

    @SchemaMapping(typeName = "Video", field = "categories")
    @Secured({Roles.ROLE_ADMIN, Roles.ROLE_SUBSCRIBER, Roles.ROLE_VIDEOS})
    public List<GqlCategory> categories(final GqlVideo video) {
        return getAllCategoryByIdUseCase.execute(
                        new GetAllCategoryByIdUseCase.Input(video.categoriesId())
                ).stream()
                .map(GqlCategoryPresenter::present)
                .toList();
    }

    @SchemaMapping(typeName = "Video", field = "genres")
    @Secured({Roles.ROLE_ADMIN, Roles.ROLE_SUBSCRIBER, Roles.ROLE_VIDEOS})
    public List<GqlGenre> genres(final GqlVideo video) {
        return getAllGenreByIdUseCase.execute(
                        new GetAllGenreByIdUseCase.Input(video.genresId())
                ).stream()
                .map(GqlGenrePresenter::present)
                .toList();
    }

    @MutationMapping
    @Secured({Roles.ROLE_ADMIN, Roles.ROLE_VIDEOS})
    public SaveVideoUseCase.Output saveVideo(@Argument(name = "input") final GqlVideoInput videoInput) {
        final var input = new SaveVideoUseCase.Input(
                videoInput.id(),
                videoInput.title(),
                videoInput.description(),
                videoInput.yearLaunched(),
                videoInput.duration(),
                videoInput.rating(),
                videoInput.opened(),
                videoInput.published(),
                videoInput.createdAt(),
                videoInput.updatedAt(),
                videoInput.video(),
                videoInput.trailer(),
                videoInput.banner(),
                videoInput.thumbnail(),
                videoInput.thumbnailHalf(),
                videoInput.categoriesId(),
                videoInput.castMembersId(),
                videoInput.genresId()
        );
        return saveVideoUseCase.execute(input);
    }

}
