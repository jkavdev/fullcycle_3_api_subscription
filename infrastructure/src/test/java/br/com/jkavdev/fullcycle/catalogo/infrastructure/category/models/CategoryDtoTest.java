package br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models;

import br.com.jkavdev.fullcycle.catalogo.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

@JacksonTest
class CategoryDtoTest {

    @Autowired
    private JacksonTester<CategoryDto> json;

    @Test
    public void testUnmarshall() throws IOException {
        final var expectedResponse = """
                {
                    "id": "a351776240cf41eab9bffbcf0f28f751",
                    "name": "Documentario",
                    "description": "categoria de documentos",
                    "is_active": true,
                    "created_at": "2025-02-11T05:51:10.102949Z",
                    "updated_at": "2025-02-11T05:51:10.102949Z",
                    "deleted_at": null
                }
                """;

        final var actualCategory = json.parse(expectedResponse);

        Assertions.assertThat(actualCategory)
                .hasFieldOrPropertyWithValue("id", "a351776240cf41eab9bffbcf0f28f751")
                .hasFieldOrPropertyWithValue("name", "Documentario")
                .hasFieldOrPropertyWithValue("description", "categoria de documentos")
                .hasFieldOrPropertyWithValue("isActive", true)
                .hasFieldOrPropertyWithValue("createdAt", Instant.parse("2025-02-11T05:51:10.102949Z"))
                .hasFieldOrPropertyWithValue("updatedAt", Instant.parse("2025-02-11T05:51:10.102949Z"))
                .hasFieldOrPropertyWithValue("deletedAt", null)
        ;

    }

}