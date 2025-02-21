package br.com.jkavdev.fullcycle.catalogo.application.category.delete;

import br.com.jkavdev.fullcycle.catalogo.application.UnitUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategoryGateway;

import java.util.Objects;

public class DeleteCategoryUseCase extends UnitUseCase<String> {

    private final CategoryGateway categoryGateway;

    public DeleteCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public void execute(final String anId) {
        if (anId == null) {
            return;
        }

        categoryGateway.deleteById(anId);
    }
}
