package br.com.jkavdev.fullcycle.catalogo.infrastructure.video;

import br.com.jkavdev.fullcycle.catalogo.AbstractElasticsearchTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.IdUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Rating;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Video;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.persistence.VideoDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.persistence.VideoRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.Collections;
import java.util.Set;

class VideoElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoElasticsearchGateway videoGateway;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(videoRepository);
        Assertions.assertNotNull(videoGateway);
    }

    @Test
    public void givenValidInput_whenCallsSave_shouldPersistIt() {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = true;
        final var expectedPublished = true;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo = "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumbnail = "http://thumbnail";
        final var expectedThumbnailHalf = "http://thumbnailhalf";

        Assertions.assertEquals(0, videoRepository.count());

        // when
        final var input = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedRating.getName(),
                expectedOpened,
                expectedPublished,
                expectedCreatedAt.toString(),
                expectedUpdatedAt.toString(),
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                expectedTrailer,
                expectedVideo,
                expectedCategories,
                expectedMembers,
                expectedGenres
        );

        final var actualOutput = videoGateway.save(input);

        // then
        Assertions.assertEquals(1, videoRepository.count());

        Assertions.assertEquals(input, actualOutput);

        final var actualVideo = videoRepository.findById(expectedId).orElseThrow();
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedCreatedAt.toString(), actualVideo.createdAt());
        Assertions.assertEquals(expectedUpdatedAt.toString(), actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt.getValue(), actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating.getName(), actualVideo.rating());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumbnail, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());

    }

    @Test
    public void givenMinimalInput_whenCallsSave_shouldPersistIt() {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "qualquerTitulo";
        final var expectedRating = Fixture.Videos.rating();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final String expectedDescription = null;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final String expectedVideo = null;
        final String expectedBanner = null;
        final String expectedTrailer = null;
        final String expectedThumbnail = null;
        final String expectedThumbnailHalf = null;
        final var expectedMembers = Collections.<String>emptySet();
        final var expectedCategories = Collections.<String>emptySet();
        final var expectedGenres = Collections.<String>emptySet();
        final var expectedDate = InstantUtils.now();

        Assertions.assertEquals(0, videoRepository.count());

        // when
        final var input = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedRating.getName(),
                expectedOpened,
                expectedPublished,
                expectedDate.toString(),
                expectedDate.toString(),
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                expectedTrailer,
                expectedVideo,
                expectedCategories,
                expectedMembers,
                expectedGenres
        );

        final var actualOutput = videoGateway.save(input);

        // then
        Assertions.assertEquals(1, videoRepository.count());

        Assertions.assertEquals(input, actualOutput);

        final var actualVideo = videoRepository.findById(expectedId).orElseThrow();
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedDate.toString(), actualVideo.createdAt());
        Assertions.assertEquals(expectedDate.toString(), actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt.getValue(), actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating.getName(), actualVideo.rating());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumbnail, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());

    }

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var expectedVideo = Fixture.Videos.systemDesign();

        videoRepository.save(VideoDocument.from(expectedVideo));

        final var expectedId = expectedVideo.id();
        Assertions.assertTrue(videoRepository.existsById(expectedId));

        // when
        videoGateway.deleteById(expectedId);

        // then
        Assertions.assertFalse(videoRepository.existsById(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "qualquerId";

        // when
        // then
        Assertions.assertDoesNotThrow(() -> videoGateway.deleteById(expectedId));
    }

    @Test
    public void givenNullId_whenCallsDeleteById_shouldBeOk() {
        // given
        final String expectedId = null;

        // when
        // then
        Assertions.assertDoesNotThrow(() -> videoGateway.deleteById(expectedId));
    }

    @Test
    public void givenEmptyId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "  ";

        // when
        // then
        Assertions.assertDoesNotThrow(() -> videoGateway.deleteById(expectedId));
    }

    @Test
    public void givenVideoPersisted_whenCallsFindById_shouldRetrieveIt() {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = true;
        final var expectedPublished = true;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo = "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumbnail = "http://thumbnail";
        final var expectedThumbnailHalf = "http://thumbnailhalf";

        Assertions.assertEquals(0, videoRepository.count());

        videoRepository.save(VideoDocument.from(
                Video.with(
                        expectedId,
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchedAt.getValue(),
                        expectedDuration,
                        expectedRating.getName(),
                        expectedOpened,
                        expectedPublished,
                        expectedCreatedAt.toString(),
                        expectedUpdatedAt.toString(),
                        expectedBanner,
                        expectedThumbnail,
                        expectedThumbnailHalf,
                        expectedTrailer,
                        expectedVideo,
                        expectedCategories,
                        expectedMembers,
                        expectedGenres
                )
        ));

        // when
        final var actualVideo = videoGateway.findById(expectedId).orElseThrow();

        // then
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumbnail, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());

    }

    @Test
    public void givenIdMismatch_whenCallsFindById_shouldBeOk() {
        // given
        final var expectedId = "qualquerId";

        Assertions.assertEquals(0, videoRepository.count());

        // when
        final var actualVideo = videoGateway.findById(expectedId);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertTrue(actualVideo.isEmpty());
    }

    @Test
    public void givenNullId_whenCallsFindById_shouldBeOk() {
        // given
        final String expectedId = null;

        Assertions.assertEquals(0, videoRepository.count());

        // when
        final var actualVideo = videoGateway.findById(expectedId);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertTrue(actualVideo.isEmpty());
    }

    @Test
    public void givenEmptyId_whenCallsFindById_shouldBeOk() {
        // given
        final var expectedId = "  ";

        Assertions.assertEquals(0, videoRepository.count());

        // when
        final var actualVideo = videoGateway.findById(expectedId);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertTrue(actualVideo.isEmpty());
    }

    @Test
    public void givenEmptyVideos_whenCallsFindAll_shouldReturnEmpyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algooooo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCastMembers = Set.<String>of();
        final var expectedCategories = Set.<String>of();
        final var expectedGenres = Set.<String>of();

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                expectedRating,
                expectedYearLaunched,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        // when
        final var actualOutput = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedTotal, actualOutput.data().size()
        );
    }

    @ParameterizedTest
    @CsvSource({
            "go,0,10,1,1,Golang 1.22",
            "jav,0,10,1,1,Java 21",
            "design,0,10,1,1,System Design no Mercado Livre na prática",
            "assistido,0,10,1,1,System Design no Mercado Livre na prática",
            "FTW,0,10,1,1,Java 21",
            "linguagem,0,10,1,1,Golang 1.22",
    })
    public void givenValidTerm_whenCallsFindAll_shouldReturnElementsFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedTitle
    ) {
        // given
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCastMembers = Set.<String>of();
        final var expectedCategories = Set.<String>of();
        final var expectedGenres = Set.<String>of();

        mockVideos();

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                expectedRating,
                expectedYearLaunched,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        // when
        final var actualOutput = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedTitle, actualOutput.data().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "meeting,0,10,1,1,Golang 1.22",
            "aulas,0,10,1,1,System Design no Mercado Livre na prática",
            "lives,0,10,1,1,Java 21",
            ",0,10,3,3,Golang 1.22",
    })
    public void givenValidCategories_whenCallsFindAll_shouldReturnElementsFiltered(
            final String category,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedTitle
    ) {
        // given
        mockVideos();

        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCategories = category == null ? Set.<String>of() : Set.of(category);
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCastMembers = Set.<String>of();
        final var expectedGenres = Set.<String>of();


        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                expectedRating,
                expectedYearLaunched,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        // when
        final var actualOutput = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedTitle, actualOutput.data().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "gabriel,0,10,1,1,Java 21",
            "wesley,0,10,1,1,Golang 1.22",
            "luiz,0,10,1,1,System Design no Mercado Livre na prática",
            ",0,10,3,3,Golang 1.22",
    })
    public void givenValidCastMembers_whenCallsFindAll_shouldReturnElementsFiltered(
            final String castMember,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedTitle
    ) {
        // given
        mockVideos();

        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCategories = Set.<String>of();
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCastMembers = castMember == null ? Set.<String>of() : Set.of(castMember);
        final var expectedGenres = Set.<String>of();


        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                expectedRating,
                expectedYearLaunched,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        // when
        final var actualOutput = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedTitle, actualOutput.data().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "java,0,10,1,1,Java 21",
            "golang,0,10,1,1,Golang 1.22",
            "systemdesign,0,10,1,1,System Design no Mercado Livre na prática",
            ",0,10,3,3,Golang 1.22",
    })
    public void givenValidGenres_whenCallsFindAll_shouldReturnElementsFiltered(
            final String genre,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedTitle
    ) {
        // given
        mockVideos();

        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCategories = Set.<String>of();
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCastMembers = Set.<String>of();
        final var expectedGenres = genre == null ? Set.<String>of() : Set.of(genre);


        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                expectedRating,
                expectedYearLaunched,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        // when
        final var actualOutput = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedTitle, actualOutput.data().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "title,asc,0,10,3,3,Golang 1.22",
            "title,desc,0,10,3,3,System Design no Mercado Livre na prática",
            "created_at,asc,0,10,3,3,System Design no Mercado Livre na prática",
            "created_at,desc,0,10,3,3,Java 21",
    })
    public void givenValidSortAndDirection_whenCallsFindAll_shouldReturnElementsSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedTitle
    ) {
        // given
        mockVideos();

        final var expectedTerms = "";
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCastMembers = Set.<String>of();
        final var expectedCategories = Set.<String>of();
        final var expectedGenres = Set.<String>of();


        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                expectedRating,
                expectedYearLaunched,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        // when
        final var actualOutput = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedTitle, actualOutput.data().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "0,1,1,3,Golang 1.22",
            "1,1,1,3,Java 21",
            "2,1,1,3,System Design no Mercado Livre na prática",
            "3,1,0,3,",
    })
    public void givenValidPage_whenCallsFindAll_shouldReturnElementsPaged(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedTitle
    ) {
        // given
        mockVideos();

        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCastMembers = Set.<String>of();
        final var expectedCategories = Set.<String>of();
        final var expectedGenres = Set.<String>of();


        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                expectedRating,
                expectedYearLaunched,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        // when
        final var actualOutput = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        if (StringUtils.isNotEmpty(expectedTitle)) {
            Assertions.assertEquals(expectedTitle, actualOutput.data().get(0).title());
        }
    }

    private void mockVideos() {
        videoRepository.save(VideoDocument.from(Fixture.Videos.systemDesign()));
        videoRepository.save(VideoDocument.from(Fixture.Videos.golang()));
        videoRepository.save(VideoDocument.from(Fixture.Videos.java21()));
    }

}