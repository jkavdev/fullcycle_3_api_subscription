package br.com.jkavdev.fullcycle.catalogo.infrastructure.video;

import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Video;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.persistence.VideoDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.persistence.VideoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Profile("!development")
public class VideoElasticsearchGateway implements VideoGateway {

    private static final String TITLE_PROP = "title";
    private static final String KEYWORD = ".keyword";

    private final VideoRepository videoRepository;

    private final SearchOperations searchOperations;

    public VideoElasticsearchGateway(
            final VideoRepository videoRepository,
            final SearchOperations searchOperations
    ) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Video save(final Video video) {
        videoRepository.save(VideoDocument.from(video));
        return video;
    }

    @Override
    public void deleteById(final String anId) {
        if (anId == null || anId.isBlank()) {
            return;
        }
        videoRepository.deleteById(anId);
    }

    @Override
    public Optional<Video> findById(final String anId) {
        if (anId == null || anId.isBlank()) {
            return Optional.empty();
        }
        return videoRepository.findById(anId)
                .map(VideoDocument::toVideo);
    }

    @Override
    public Pagination<Video> findAll(final VideoSearchQuery aQuery) {
        final var currentPage = aQuery.page();
        final var itemsPerPage = aQuery.perPage();

        final var sort =
                Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));
        final var page = PageRequest.of(currentPage, itemsPerPage, sort);

        final var aQueryBuilder = new VideoQueryBuilder(
                VideoQueryBuilder.onlyPublished(),
                VideoQueryBuilder.containingCastMembers(aQuery.castMembers()),
                VideoQueryBuilder.containingCategories(aQuery.categories()),
                VideoQueryBuilder.containingGenres(aQuery.genres()),
                VideoQueryBuilder.launchedAtEquals(aQuery.launchedAt()),
                VideoQueryBuilder.ratingEquals(aQuery.rating()),
                VideoQueryBuilder.titleOrDescriptionContaining(aQuery.terms())
        );

        final var query = NativeQuery.builder()
                .withQuery(aQueryBuilder.build())
                .withPageable(page)
                .build();

        final var res = searchOperations.search(query, VideoDocument.class);
        final var total = res.getTotalHits();
        final var videos = res.stream()
                .map(SearchHit::getContent)
                .map(VideoDocument::toVideo)
                .toList();
        return new Pagination<>(currentPage, itemsPerPage, total, videos);
    }

    private String buildSort(final String sort) {
        if (TITLE_PROP.equals(sort)) {
            return sort.concat(KEYWORD);
        } else {
            return sort;
        }
    }

}
