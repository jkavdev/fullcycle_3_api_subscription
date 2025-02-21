package br.com.jkavdev.fullcycle.catalogo.application.castmember.delete;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenValidId_whenCallsDelete_shouldBeOk() {
        // given
        final var aMember = Fixture.CastMembers.gabriel();
        final var expectedId = aMember.id();

        Mockito.doNothing()
                .when(castMemberGateway)
                .deleteById(ArgumentMatchers.anyString());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId));

        // then
        Mockito.verify(castMemberGateway, Mockito.times(1)).deleteById(expectedId);

    }

    @Test
    public void givenInvalidId_whenCallsDelete_shouldBeOk() {
        // given
        final String expectedId = null;

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId));

        // then
        Mockito.verify(castMemberGateway, Mockito.never()).deleteById(expectedId);

    }

}
