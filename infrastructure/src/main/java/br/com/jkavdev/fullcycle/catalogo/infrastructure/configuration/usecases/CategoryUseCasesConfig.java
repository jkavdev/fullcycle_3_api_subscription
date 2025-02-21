package br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.usecases;

import br.com.jkavdev.fullcycle.catalogo.application.category.get.GetAllCategoryByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.delete.DeleteCategoryUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.list.ListCategoryUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.save.SaveCategoryUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration(enforceUniqueMethods = false)
public class CategoryUseCasesConfig {

    private final CategoryGateway categoryGateway;

    public CategoryUseCasesConfig(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DeleteCategoryUseCase(categoryGateway);
    }

    @Bean
    SaveCategoryUseCase saveCategoryUseCase() {
        return new SaveCategoryUseCase(categoryGateway);
    }

    @Bean
    ListCategoryUseCase listCategoryUseCase() {
        return new ListCategoryUseCase(categoryGateway);
    }

    @Bean
    GetAllCategoryByIdUseCase getAllCategoryByIdUseCase() {
        return new GetAllCategoryByIdUseCase(categoryGateway);
    }
}
