package br.com.jkavdev.fullcycle.catalogo.application.category.get;

import br.com.jkavdev.fullcycle.catalogo.application.UseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategoryGateway;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GetAllCategoryByIdUseCase extends UseCase<GetAllCategoryByIdUseCase.Input, List<GetAllCategoryByIdUseCase.Output>> {

    private final CategoryGateway categoryGateway;

    public GetAllCategoryByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public List<Output> execute(Input input) {
        if (input.ids().isEmpty()) {
            return Collections.emptyList();
        }
        return categoryGateway.findAllById(input.ids()).stream()
                .map(Output::new)
                .toList();
    }

    public record Input(Set<String> ids) {
        @Override
        public Set<String> ids() {
            return ids != null ? ids : Collections.emptySet();
        }
    }

    public record Output(
            String id,
            String name,
            String description
    ) {

        public Output(final Category aCategory) {
            this(
                    aCategory.id(),
                    aCategory.name(),
                    aCategory.description()
            );
        }
    }

}
