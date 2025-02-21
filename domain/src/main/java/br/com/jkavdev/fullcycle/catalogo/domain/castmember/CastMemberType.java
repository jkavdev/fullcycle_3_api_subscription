package br.com.jkavdev.fullcycle.catalogo.domain.castmember;

import java.util.Arrays;

public enum CastMemberType {

    ACTOR,
    DIRECTOR,
    UNKNOWN;

    public static CastMemberType of(final String type) {
        if (type == null) {
            return UNKNOWN;
        }
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(type))
                .findFirst()
                .orElse(UNKNOWN);
    }

}
