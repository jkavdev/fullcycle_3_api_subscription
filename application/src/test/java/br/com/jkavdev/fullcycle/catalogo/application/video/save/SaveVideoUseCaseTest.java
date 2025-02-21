package br.com.jkavdev.fullcycle.catalogo.application.video.save;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.IdUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Rating;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Video;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Year;
import java.util.Set;

public class SaveVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private SaveVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

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
        final var expectedOpened = false;
        final var expectedPublished = false;
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

        Mockito.when(videoGateway.save(ArgumentMatchers.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var input = new SaveVideoUseCase.Input(
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

        final var actualOutput = useCase.execute(input);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId, actualOutput.id());

        final var captor = ArgumentCaptor.forClass(Video.class);

        Mockito.verify(videoGateway, Mockito.times(1))
                .save(captor.capture());

        final var actualVideo = captor.getValue();
        Assertions.assertNotNull(actualVideo);
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
    public void givenNullInput_whenCallsSave_shouldReturnError() {
        // given
        final SaveVideoUseCase.Input input = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'SaveVideoUseCase.Input' cannot be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(input));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(videoGateway, Mockito.never())
                .save(Mockito.any());

    }

    @Test
    public void givenInvalidIdCategory_whenCallsSave_shouldReturnError() {
        // given
        final var expectedId = " ";
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
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

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var input = new SaveVideoUseCase.Input(
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

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(input));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(videoGateway, Mockito.never())
                .save(Mockito.any());

    }

}
