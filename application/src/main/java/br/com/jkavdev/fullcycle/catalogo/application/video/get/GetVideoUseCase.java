package br.com.jkavdev.fullcycle.catalogo.application.video.get;

import br.com.jkavdev.fullcycle.catalogo.application.UseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Video;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoGateway;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class GetVideoUseCase extends UseCase<GetVideoUseCase.Input, Optional<GetVideoUseCase.Output>> {

    private final VideoGateway videoGateway;

    public GetVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Optional<Output> execute(final Input input) {
        if (input == null || input.id() == null) {
            return Optional.empty();
        }

        return videoGateway.findById(input.id())
                .map(Output::from);
    }

    public record Input(String id) {

    }

    public record Output(
            String id,
            String title,
            String description,
            Integer launchedAt,
            double duration,
            String rating,
            boolean opened,
            boolean published,
            String createdAt,
            String updatedAt,
            String banner,
            String thumbnail,
            String thumbnailHalf,
            String trailer,
            String video,
            Set<String> categories,
            Set<String> castMembers,
            Set<String> genres
    ) {

        public static Output from(final Video video) {

            return new Output(
                    video.id(),
                    video.title(),
                    video.description(),
                    video.launchedAt().getValue(),
                    video.duration(),
                    video.rating().getName(),
                    video.opened(),
                    video.published(),
                    video.createdAt().toString(),
                    video.updatedAt().toString(),
                    video.banner(),
                    video.thumbnail(),
                    video.thumbnailHalf(),
                    video.trailer(),
                    video.video(),
                    video.categories(),
                    video.castMembers(),
                    video.genres()
            );
        }
    }
}
