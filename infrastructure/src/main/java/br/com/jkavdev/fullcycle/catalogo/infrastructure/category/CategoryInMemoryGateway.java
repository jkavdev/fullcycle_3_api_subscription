package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategoryGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategorySearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("development")
public class CategoryInMemoryGateway implements CategoryGateway {

    private final ConcurrentHashMap<String, Category> db;

    public CategoryInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public Category save(Category aCategory) {
        this.db.put(aCategory.id(), aCategory);
        return aCategory;
    }

    @Override
    public void deleteById(String anId) {
        db.remove(anId);
    }

    @Override
    public Optional<Category> findById(String anId) {
        return Optional.ofNullable(db.get(anId));
    }

    @Override
    public List<Category> findAllById(Set<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return ids.stream()
                .map(db::get)
                .toList();
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        final var values = db.values();
        return new Pagination<>(aQuery.page(), aQuery.perPage(), values.size(), new ArrayList<>(values));
    }

}
