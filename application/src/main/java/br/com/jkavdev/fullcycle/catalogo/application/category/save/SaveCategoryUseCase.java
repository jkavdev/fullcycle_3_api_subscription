package br.com.jkavdev.fullcycle.catalogo.application.category.save;

import br.com.jkavdev.fullcycle.catalogo.application.UseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategoryGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.NotificationException;
import br.com.jkavdev.fullcycle.catalogo.domain.validation.Error;
import br.com.jkavdev.fullcycle.catalogo.domain.validation.handler.Notification;

import java.util.Objects;

public class SaveCategoryUseCase extends UseCase<Category, Category> {

    private final CategoryGateway categoryGateway;

    public SaveCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Category execute(final Category aCategory) {
        if (aCategory == null) {
            throw NotificationException.with(new Error("'aCategory' cannot be null"));
        }

        final var notification = Notification.create();
        aCategory.validate(notification);

        if (notification.hasError()) {
            throw NotificationException.with("Invalid category", notification);
        }

        return categoryGateway.save(aCategory);
    }
}
