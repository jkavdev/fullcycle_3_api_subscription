package br.com.jkavdev.fullcycle.catalogo.infrastructure.genre;

import br.com.jkavdev.fullcycle.catalogo.domain.genre.Genre;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.persistence.GenreDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.persistence.GenreRepository;
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
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.StreamSupport;

@Component
@Profile("!development")
public class GenreElasticsearchGateway implements GenreGateway {

    private static final String NAME_PROP = "name";
    private static final String KEYWORD = ".keyword";

    private final GenreRepository genreRepository;

    private final SearchOperations searchOperations;

    public GenreElasticsearchGateway(
            final GenreRepository genreRepository,
            final SearchOperations searchOperations
    ) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Genre save(final Genre aGenre) {
        genreRepository.save(GenreDocument.from(aGenre));
        return aGenre;
    }

    @Override
    public void deleteById(final String anId) {
        genreRepository.deleteById(anId);
    }

    @Override
    public Optional<Genre> findById(final String anId) {
        return genreRepository.findById(anId)
                .map(GenreDocument::toGenre);
    }

    @Override
    public List<Genre> findAllById(Set<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(genreRepository.findAllById(ids).spliterator(), false)
                .map(GenreDocument::toGenre)
                .toList();
    }

    @Override
    public Pagination<Genre> findAll(final GenreSearchQuery aQuery) {
        final var terms = aQuery.terms();
        final var currentPage = aQuery.page();
        final var perPage = aQuery.perPage();

        final var sort =
                Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));
        final var page = PageRequest.of(currentPage, perPage, sort);

        final var query =
                StringUtils.isEmpty(terms) && CollectionUtils.isEmpty(aQuery.categories())
                        ? Query.findAll().setPageable(page)
                        : new CriteriaQuery(createCriteria(aQuery), page);

        final var res = searchOperations.search(query, GenreDocument.class);
        final var total = res.getTotalHits();
        final var genres = res.stream()
                .map(SearchHit::getContent)
                .map(GenreDocument::toGenre)
                .toList();
        return new Pagination<>(currentPage, perPage, total, genres);
    }

    private static Criteria createCriteria(final GenreSearchQuery aQuery) {
        Criteria criteria = null;

        if (StringUtils.isNotEmpty(aQuery.terms())) {
            criteria = Criteria.where("name").contains(aQuery.terms());
        }
        if (!CollectionUtils.isEmpty(aQuery.categories())) {
            final var categoriesWhere = Criteria.where("categories").in(aQuery.categories());
            criteria = criteria != null
                    ? criteria.and(categoriesWhere)
                    : categoriesWhere;
        }

        return criteria;
    }

    private String buildSort(final String sort) {
        if (NAME_PROP.equals(sort)) {
            return sort.concat(KEYWORD);
        } else {
            return sort;
        }
    }
}
