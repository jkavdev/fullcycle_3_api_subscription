package br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.models;

import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMember;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberType;

import java.time.Instant;

public record GqlCastMemberInput(
        String id,
        String name,
        String type,
        Instant createdAt,
        Instant updatedAt
) {
    public CastMember toCastMember() {
        return CastMember.with(id, name, CastMemberType.of(type), createdAt, updatedAt);
    }
}
