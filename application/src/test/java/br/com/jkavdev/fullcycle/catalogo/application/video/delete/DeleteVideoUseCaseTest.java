package br.com.jkavdev.fullcycle.catalogo.application.video.delete;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class DeleteVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Test
    public void givenValidId_whenCallsDelete_shouldBeOk() {
        // given
        final var systemDesign = Fixture.Videos.systemDesign();
        final var expectedId = systemDesign.id();

        Mockito.doNothing()
                .when(videoGateway)
                .deleteById(ArgumentMatchers.anyString());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(new DeleteVideoUseCase.Input(expectedId)));

        // then
        Mockito.verify(videoGateway, Mockito.times(1)).deleteById(expectedId);

    }

    @Test
    public void givenNullInput_whenCallsDelete_shouldBeOk() {
        // given
        final DeleteVideoUseCase.Input expectedInput = null;

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedInput));

        // then
        Mockito.verify(videoGateway, Mockito.never()).deleteById(Mockito.anyString());

    }

    @Test
    public void givenInvalidId_whenCallsDelete_shouldBeOk() {
        // given
        final String expectedId = null;

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(new DeleteVideoUseCase.Input(expectedId)));

        // then
        Mockito.verify(videoGateway, Mockito.never()).deleteById(expectedId);

    }

}
