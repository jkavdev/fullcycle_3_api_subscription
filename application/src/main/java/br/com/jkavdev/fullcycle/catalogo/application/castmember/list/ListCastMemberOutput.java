package br.com.jkavdev.fullcycle.catalogo.application.castmember.list;

import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMember;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberType;

import java.time.Instant;

public record ListCastMemberOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt,
        Instant updatedAt
) {

    public static ListCastMemberOutput from(final CastMember aMember) {
        return new ListCastMemberOutput(
                aMember.id(),
                aMember.name(),
                aMember.type(),
                aMember.createdAt(),
                aMember.updatedAt()
        );
    }
}
