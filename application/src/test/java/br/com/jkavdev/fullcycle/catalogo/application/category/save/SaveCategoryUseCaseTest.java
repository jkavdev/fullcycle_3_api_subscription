package br.com.jkavdev.fullcycle.catalogo.application.category.save;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategoryGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.UUID;

public class SaveCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private SaveCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenValidCategory_whenCallsSave_shouldPersistIt() {
        // given
        final var aCategory = Fixture.Categories.aulas();

        Mockito.when(categoryGateway.save(ArgumentMatchers.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        useCase.execute(aCategory);

        // then
        Mockito.verify(categoryGateway, Mockito.times(1))
                .save(aCategory);

    }

    @Test
    public void givenNullCategory_whenCallsSave_shouldReturnError() {
        // given
        final Category aCategory = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'aCategory' cannot be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(aCategory));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.never())
                .save(aCategory);

    }

    @Test
    public void givenInvalidNameCategory_whenCallsSave_shouldReturnError() {
        // given
        final var aCategory = Category.with(
                UUID.randomUUID().toString().replace("-", ""),
                null,
                "Conteudo gravado",
                true,
                InstantUtils.now(),
                InstantUtils.now(),
                null
        );
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(aCategory));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.never())
                .save(aCategory);

    }

    @Test
    public void givenInvalidIdCategory_whenCallsSave_shouldReturnError() {
        // given
        final var aCategory = Category.with(
                null,
                "Aula",
                "Conteudo gravado",
                true,
                InstantUtils.now(),
                InstantUtils.now(),
                null
        );
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(aCategory));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.never())
                .save(aCategory);

    }

}
