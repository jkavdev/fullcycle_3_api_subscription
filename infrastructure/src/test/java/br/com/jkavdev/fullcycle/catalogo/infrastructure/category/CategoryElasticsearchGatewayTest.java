package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.AbstractElasticsearchTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategorySearchQuery;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.persistence.CategoryDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Set;

public class CategoryElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryElasticsearchGateway categoryGateway;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(categoryRepository);
        Assertions.assertNotNull(categoryGateway);
    }

    @Test
    public void givenValidCategory_whenCallsSave_shouldPersistIt() {
        // given
        final var expectedCategory = Fixture.Categories.aulas();

        // when
        final var actualOutput = categoryGateway.save(expectedCategory);

        // then
        Assertions.assertEquals(expectedCategory, actualOutput);

        final var actualCategory = categoryRepository.findById(expectedCategory.id())
                .orElseThrow();
        Assertions.assertEquals(expectedCategory.id(), actualCategory.id());
        Assertions.assertEquals(expectedCategory.name(), actualCategory.name());
        Assertions.assertEquals(expectedCategory.description(), actualCategory.description());
        Assertions.assertEquals(expectedCategory.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(expectedCategory.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(expectedCategory.deletedAt(), actualCategory.deletedAt());
    }

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var expectedCategory = Fixture.Categories.aulas();

        categoryRepository.save(CategoryDocument.from(expectedCategory));

        final var expectedId = expectedCategory.id();
        Assertions.assertTrue(categoryRepository.existsById(expectedId));

        // when
        categoryGateway.deleteById(expectedId);

        // then
        Assertions.assertFalse(categoryRepository.existsById(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "qualquerId";

        // when
        // then
        Assertions.assertDoesNotThrow(() -> categoryGateway.deleteById(expectedId));
    }

    @Test
    public void givenValidId_whenCallsFindById_shouldRetrieveIt() {
        // given
        final var expectedCategory = Fixture.Categories.talks();

        categoryRepository.save(CategoryDocument.from(expectedCategory));

        final var expectedId = expectedCategory.id();
        final var actualOutput = categoryRepository.findById(expectedId)
                .orElseThrow();

        // when
        categoryGateway.findById(expectedId);

        // then
        Assertions.assertEquals(expectedCategory.id(), actualOutput.id());
        Assertions.assertEquals(expectedCategory.name(), actualOutput.name());
        Assertions.assertEquals(expectedCategory.description(), actualOutput.description());
        Assertions.assertEquals(expectedCategory.createdAt(), actualOutput.createdAt());
        Assertions.assertEquals(expectedCategory.updatedAt(), actualOutput.updatedAt());
        Assertions.assertEquals(expectedCategory.deletedAt(), actualOutput.deletedAt());
    }

    @Test
    public void givenValidId_whenCallsFindById_shouldReturnEmpy() {
        // given
        final var expectedId = "qualquerId";

        // when
        // then
        Assertions.assertTrue(categoryGateway.findById(expectedId).isEmpty());
    }

    @Test
    public void givenEmptyCategories_whenCallsFindAll_shouldReturnEmpyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algooooo";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = categoryGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedTotal, actualOutput.data().size()
        );
    }

    @ParameterizedTest
    @CsvSource({
            "aul,0,10,1,1,Aulas",
            "liv,0,10,1,1,Lives"
    })
    public void givenValidTerm_whenCallsFindAll_shouldReturnElementsFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        mockCategories();

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = categoryGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,3,3,Aulas",
            "name,desc,0,10,3,3,Talks",
            "created_at,asc,0,10,3,3,Aulas",
            "created_at,desc,0,10,3,3,Talks",
    })
    public void givenValidSortAndDirection_whenCallsFindAll_shouldReturnElementsSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        final var expectedTerms = "";

        mockCategories();

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = categoryGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,1,1,3,Aulas",
            "1,1,1,3,Lives",
            "2,1,1,3,Talks",
            "3,1,0,3,",
    })
    public void givenValidPage_whenCallsFindAll_shouldReturnElementsPaged(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        mockCategories();

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = categoryGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        if (StringUtils.isNotEmpty(expectedName)) {
            Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
        }
    }

    @Test
    public void givenValidIds_whenCallsFindAllByIds_shouldReturnElements() {
        // given
        final var talks = categoryRepository.save(CategoryDocument.from(Fixture.Categories.talks()));
        categoryRepository.save(CategoryDocument.from(Fixture.Categories.lives()));
        final var aulas = categoryRepository.save(CategoryDocument.from(Fixture.Categories.aulas()));

        final var expectedSize = 2;
        final var expectedIds = Set.of(talks.id(), aulas.id());

        // when
        final var actualOutput = categoryGateway.findAllById(expectedIds);

        // then
        Assertions.assertEquals(expectedSize, actualOutput.size());

        final var actualIds = actualOutput.stream().map(Category::id).toList();
        Assertions.assertTrue(expectedIds.containsAll(actualIds));
    }

    @Test
    public void givenNullIds_whenCallsFindAllByIds_shouldReturnEmpty() {
        // given
        final Set<String> expectedIds = null;

        // when
        final var actualOutput = categoryGateway.findAllById(expectedIds);

        // then
        Assertions.assertTrue(actualOutput.isEmpty());
    }

    @Test
    public void givenEmptyIds_whenCallsFindAllByIds_shouldReturnEmpty() {
        // given
        final Set<String> expectedIds = Collections.emptySet();

        // when
        final var actualOutput = categoryGateway.findAllById(expectedIds);

        // then
        Assertions.assertTrue(actualOutput.isEmpty());
    }

    private void mockCategories() {
        categoryRepository.save(CategoryDocument.from(Fixture.Categories.aulas()));
        categoryRepository.save(CategoryDocument.from(Fixture.Categories.lives()));
        categoryRepository.save(CategoryDocument.from(Fixture.Categories.talks()));
    }

}
