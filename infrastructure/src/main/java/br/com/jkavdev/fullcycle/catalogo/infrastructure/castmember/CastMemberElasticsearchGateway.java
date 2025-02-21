package br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember;

import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMember;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
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
public class CastMemberElasticsearchGateway implements CastMemberGateway {

    private static final String NAME_PROP = "name";
    private static final String KEYWORD = ".keyword";

    private final CastMemberRepository castMemberRepository;

    private final SearchOperations searchOperations;

    public CastMemberElasticsearchGateway(
            final CastMemberRepository castMemberRepository,
            final SearchOperations searchOperations
    ) {
        this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public CastMember save(final CastMember aMember) {
        castMemberRepository.save(CastMemberDocument.from(aMember));
        return aMember;
    }

    @Override
    public void deleteById(final String anId) {
        castMemberRepository.deleteById(anId);
    }

    @Override
    public Optional<CastMember> findById(final String anId) {
        return castMemberRepository.findById(anId)
                .map(CastMemberDocument::toCastMember);
    }

    @Override
    public List<CastMember> findAllById(final Set<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(castMemberRepository.findAllById(ids).spliterator(), false)
                .map(CastMemberDocument::toCastMember)
                .toList();
    }

    @Override
    public Pagination<CastMember> findAll(final CastMemberSearchQuery aQuery) {
        final var terms = aQuery.terms();
        final var currentPage = aQuery.page();
        final var perPage = aQuery.perPage();

        final var sort =
                Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));
        final var page = PageRequest.of(currentPage, perPage, sort);

        final Query query = StringUtils.isNotEmpty(terms)
                ? new CriteriaQuery(Criteria.where("name").contains(terms), page)
                : Query.findAll().setPageable(page);

        final var res = searchOperations.search(query, CastMemberDocument.class);
        final var total = res.getTotalHits();
        final var castMembers = res.stream()
                .map(SearchHit::getContent)
                .map(CastMemberDocument::toCastMember)
                .toList();
        return new Pagination<>(currentPage, perPage, total, castMembers);
    }

    private String buildSort(final String sort) {
        if (NAME_PROP.equals(sort)) {
            return sort.concat(KEYWORD);
        } else {
            return sort;
        }
    }

}
