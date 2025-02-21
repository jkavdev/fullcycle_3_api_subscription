package br.com.jkavdev.fullcycle.catalogo.application.castmember.list;

import br.com.jkavdev.fullcycle.catalogo.application.UseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;

import java.util.Objects;

public class ListCastMemberUseCase extends UseCase<CastMemberSearchQuery, Pagination<ListCastMemberOutput>> {

    private final CastMemberGateway castMemberGateway;

    public ListCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public Pagination<ListCastMemberOutput> execute(final CastMemberSearchQuery aQuery) {
        return castMemberGateway.findAll(aQuery)
                .map(ListCastMemberOutput::from);
    }
}
