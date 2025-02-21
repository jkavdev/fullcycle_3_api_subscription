package br.com.jkavdev.fullcycle.catalogo.application.category.get;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class GetAllCategoryByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetAllCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenValidQuery_whenCallsAllById_shouldReturnIt() {
        // given
        final var members = List.of(
                Fixture.Categories.aulas(),
                Fixture.Categories.talks()
        );

        final var expectedIds = members.stream().map(Category::id).collect(Collectors.toSet());

        final var expectedItems = members.stream()
                .map(GetAllCategoryByIdUseCase.Output::new)
                .toList();

        Mockito.when(categoryGateway.findAllById(ArgumentMatchers.any()))
                .thenReturn(members);

        // when
        final var actualOutput = useCase.execute(new GetAllCategoryByIdUseCase.Input(expectedIds));

        // then
        Assertions.assertTrue(
                expectedItems.size() == actualOutput.size()
                        && expectedItems.containsAll(actualOutput)
        );

        Mockito.verify(categoryGateway, Mockito.times(1)).findAllById(expectedIds);

    }

    @Test
    public void givenNullIds_whenCallsAllById_shouldReturnEmpty() {
        // given
        final Set<String> expectedIds = null;

        // when
        final var actualOutput = useCase.execute(new GetAllCategoryByIdUseCase.Input(expectedIds));

        // then
        Assertions.assertTrue(actualOutput.isEmpty());

        Mockito.verify(categoryGateway, Mockito.never()).findAllById(ArgumentMatchers.any());

    }

}