package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategoryGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategorySearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.persistence.CategoryDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.StreamSupport;

@Component
@Profile("!development")
public class CategoryElasticsearchGateway implements CategoryGateway {

    private static final String NAME_PROP = "name";
    private static final String KEYWORD = ".keyword";

    private final CategoryRepository categoryRepository;

    private final SearchOperations searchOperations;

    public CategoryElasticsearchGateway(
            final CategoryRepository categoryRepository,
            final SearchOperations searchOperations
    ) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Category save(final Category aCategory) {
        categoryRepository.save(CategoryDocument.from(aCategory));
        return aCategory;
    }

    @Override
    public void deleteById(final String anId) {
        categoryRepository.deleteById(anId);
    }

    @Override
    public Optional<Category> findById(final String anId) {
        return categoryRepository.findById(anId)
                .map(CategoryDocument::toCategory);
    }

    @Override
    public List<Category> findAllById(Set<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(categoryRepository.findAllById(ids).spliterator(), false)
                .map(CategoryDocument::toCategory)
                .toList();
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        final var terms = aQuery.terms();
        final var currentPage = aQuery.page();
        final var perPage = aQuery.perPage();

        final var sort =
                Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));
        final var page = PageRequest.of(currentPage, perPage, sort);

        final Query query;
        if (StringUtils.isNotEmpty(terms)) {
            final var criteia = Criteria.where("name").contains(terms)
                    .or(Criteria.where("description").contains(terms));
            query = new CriteriaQuery(criteia, page);
        } else {
            query = Query.findAll()
                    .setPageable(page);
        }

        final var res = searchOperations.search(query, CategoryDocument.class);
        final var total = res.getTotalHits();
        final var categories = res.stream()
                .map(SearchHit::getContent)
                .map(CategoryDocument::toCategory)
                .toList();
        return new Pagination<>(currentPage, perPage, total, categories);
    }

    private String buildSort(final String sort) {
        if (NAME_PROP.equals(sort)) {
            return sort.concat(KEYWORD);
        } else {
            return sort;
        }
    }
}
