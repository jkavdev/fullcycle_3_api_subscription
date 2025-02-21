package br.com.jkavdev.fullcycle.catalogo.application.category.delete;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class DeleteCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenValidId_whenCallsDelete_shouldBeOk() {
        // given
        final var aulas = Fixture.Categories.aulas();
        final var expectedId = aulas.id();

        Mockito.doNothing()
                .when(categoryGateway)
                .deleteById(ArgumentMatchers.anyString());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId));

        // then
        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(expectedId);

    }

    @Test
    public void givenInvalidId_whenCallsDelete_shouldBeOk() {
        // given
        final String expectedId = null;

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId));

        // then
        Mockito.verify(categoryGateway, Mockito.never()).deleteById(expectedId);

    }

}
