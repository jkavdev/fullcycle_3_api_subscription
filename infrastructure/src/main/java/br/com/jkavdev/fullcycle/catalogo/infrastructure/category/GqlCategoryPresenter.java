package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.application.category.get.GetAllCategoryByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.list.ListCategoryOutput;
import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models.GqlCategory;

public final class GqlCategoryPresenter {

    private GqlCategoryPresenter() {

    }

    public static GqlCategory present(final ListCategoryOutput out) {
        return new GqlCategory(out.id(), out.name(), out.description());
    }

    public static GqlCategory present(final GetAllCategoryByIdUseCase.Output out) {
        return new GqlCategory(out.id(), out.name(), out.description());
    }

    public static GqlCategory present(final Category category) {
        return new GqlCategory(category.id(), category.name(), category.description());
    }

}
