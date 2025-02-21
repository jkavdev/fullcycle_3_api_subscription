package br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.models;

import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMember;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberType;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CastMemberEvent(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("created_at") Long createdAt,
        @JsonProperty("updated_at") Long updatedAt
) {
    public CastMember toCastMember() {
        return CastMember.with(
                id,
                name,
                CastMemberType.of(type),
                InstantUtils.fromTimestamp(createdAt),
                InstantUtils.fromTimestamp(updatedAt)
        );
    }

    public static CastMemberEvent from(final CastMember aMember) {
        return new CastMemberEvent(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toEpochMilli(),
                aMember.updatedAt().toEpochMilli()
        );
    }
}
