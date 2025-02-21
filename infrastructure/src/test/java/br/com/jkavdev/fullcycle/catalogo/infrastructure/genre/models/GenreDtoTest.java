package br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models;

import br.com.jkavdev.fullcycle.catalogo.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;
import java.util.Set;

@JacksonTest
class GenreDtoTest {

    @Autowired
    private JacksonTester<GenreDto> json;

    @Test
    public void testUnmarshall() throws IOException {
        final var expectedResponse = """
                {
                    "id": "874ba081d56447339c92addc44ba9070",
                    "name": "Banidos do velho oeste",
                    "categories_id": [
                        "a351776240cf41eab9bffbcf0f28f751"
                    ],
                    "is_active": true,
                    "created_at": "2025-02-11T05:52:16.711251Z",
                    "updated_at": "2025-02-11T05:52:16.711251Z",
                    "deleted_at": null
                }
                """;

        final var actualGenre = json.parse(expectedResponse);

        Assertions.assertThat(actualGenre)
                .hasFieldOrPropertyWithValue("id", "874ba081d56447339c92addc44ba9070")
                .hasFieldOrPropertyWithValue("name", "Banidos do velho oeste")
                .hasFieldOrPropertyWithValue("categoriesId", Set.of("a351776240cf41eab9bffbcf0f28f751"))
                .hasFieldOrPropertyWithValue("isActive", true)
                .hasFieldOrPropertyWithValue("createdAt", Instant.parse("2025-02-11T05:52:16.711251Z"))
                .hasFieldOrPropertyWithValue("updatedAt", Instant.parse("2025-02-11T05:52:16.711251Z"))
                .hasFieldOrPropertyWithValue("deletedAt", null)
        ;

    }

}