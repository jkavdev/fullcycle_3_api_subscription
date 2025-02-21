package br.com.jkavdev.fullcycle.catalogo.application.video.get;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

public class GetVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Test
    public void givenValidVideo_whenCallsGet_shouldReturnIt() {
        // given
        final var expectedVideo = Fixture.Videos.java21();

        Mockito.doReturn(Optional.of(expectedVideo))
                .when(videoGateway)
                .findById(Mockito.anyString());

        // when
        final var actualOutput = useCase.execute(new GetVideoUseCase.Input(expectedVideo.id()))
                .orElseThrow();

        // then
        Mockito.verify(videoGateway, Mockito.times(1))
                .findById(ArgumentMatchers.eq(expectedVideo.id()));

        Assertions.assertEquals(expectedVideo.id(), actualOutput.id());
        Assertions.assertEquals(expectedVideo.createdAt().toString(), actualOutput.createdAt());
        Assertions.assertEquals(expectedVideo.updatedAt().toString(), actualOutput.updatedAt());
        Assertions.assertEquals(expectedVideo.title(), actualOutput.title());
        Assertions.assertEquals(expectedVideo.description(), actualOutput.description());
        Assertions.assertEquals(expectedVideo.launchedAt().getValue(), actualOutput.launchedAt());
        Assertions.assertEquals(expectedVideo.duration(), actualOutput.duration());
        Assertions.assertEquals(expectedVideo.opened(), actualOutput.opened());
        Assertions.assertEquals(expectedVideo.published(), actualOutput.published());
        Assertions.assertEquals(expectedVideo.rating().getName(), actualOutput.rating());
        Assertions.assertEquals(expectedVideo.categories(), actualOutput.categories());
        Assertions.assertEquals(expectedVideo.genres(), actualOutput.genres());
        Assertions.assertEquals(expectedVideo.castMembers(), actualOutput.castMembers());
        Assertions.assertEquals(expectedVideo.video(), actualOutput.video());
        Assertions.assertEquals(expectedVideo.trailer(), actualOutput.trailer());
        Assertions.assertEquals(expectedVideo.banner(), actualOutput.banner());
        Assertions.assertEquals(expectedVideo.thumbnail(), actualOutput.thumbnail());
        Assertions.assertEquals(expectedVideo.thumbnailHalf(), actualOutput.thumbnailHalf());
    }

    @Test
    public void givenNullInput_whenCallsGet_shouldBeOk() {
        // given
        final GetVideoUseCase.Input expectedInput = null;

        // when
        final var actualVideo = Assertions.assertDoesNotThrow(() -> useCase.execute(expectedInput));

        // then
        Mockito.verify(videoGateway, Mockito.never())
                .deleteById(ArgumentMatchers.any());

        Assertions.assertTrue(actualVideo.isEmpty());

    }

    @Test
    public void givenInvalidId_whenCallsGet_shouldBeOk() {
        // given
        final String expectedId = null;

        // when
        final var actualVideo =
                Assertions.assertDoesNotThrow(() -> useCase.execute(new GetVideoUseCase.Input(expectedId)));

        // then
        Mockito.verify(videoGateway, Mockito.never())
                .deleteById(ArgumentMatchers.any());

        Assertions.assertTrue(actualVideo.isEmpty());

    }

}
