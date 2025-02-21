package br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember;

import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMember;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("development")
public class CastMemberInMemoryGateway implements CastMemberGateway {

    private final ConcurrentHashMap<String, CastMember> db;

    public CastMemberInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public CastMember save(CastMember aCastMember) {
        this.db.put(aCastMember.id(), aCastMember);
        return aCastMember;
    }

    @Override
    public void deleteById(String anId) {
        db.remove(anId);
    }

    @Override
    public Optional<CastMember> findById(String anId) {
        return Optional.ofNullable(db.get(anId));
    }

    @Override
    public List<CastMember> findAllById(Set<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return ids.stream()
                .map(db::get)
                .toList();
    }

    @Override
    public Pagination<CastMember> findAll(CastMemberSearchQuery aQuery) {
        final var values = db.values();
        return new Pagination<>(aQuery.page(), aQuery.perPage(), values.size(), new ArrayList<>(values));
    }

}
