package br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryEvent(
        @JsonProperty("id") String id
) {
}
