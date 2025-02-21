package br.com.jkavdev.fullcycle.catalogo.infrastructure.genre;

import br.com.jkavdev.fullcycle.catalogo.AbstractElasticsearchTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.Genre;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.IdUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.persistence.GenreDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class GenreElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreElasticsearchGateway genreGateway;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(genreRepository);
        Assertions.assertNotNull(genreGateway);
    }

    @Test
    public void givenActiveGenreWithGenres_whenCallsSave_shouldPersistIt() {
        // given
        final var expectedGenre = Genre.with(
                IdUtils.uniqueId(),
                "Business",
                true,
                Set.of("c1", "c2"),
                InstantUtils.now(),
                InstantUtils.now(),
                null
        );

        // when
        final var actualOutput = genreGateway.save(expectedGenre);

        // then
        Assertions.assertEquals(expectedGenre, actualOutput);

        final var actualGenre = genreRepository.findById(expectedGenre.id())
                .orElseThrow();
        Assertions.assertEquals(expectedGenre.id(), actualGenre.id());
        Assertions.assertEquals(expectedGenre.name(), actualGenre.name());
        Assertions.assertEquals(expectedGenre.active(), actualGenre.active());
        Assertions.assertEquals(expectedGenre.categories(), actualGenre.categories());
        Assertions.assertEquals(expectedGenre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(expectedGenre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(expectedGenre.deletedAt(), actualGenre.deletedAt());
    }

    @Test
    public void givenInactiveGenreWithoutGenres_whenCallsSave_shouldPersistIt() {
        // given
        final var expectedGenre = Genre.with(
                IdUtils.uniqueId(),
                "Business",
                false,
                new HashSet<>(),
                InstantUtils.now(),
                InstantUtils.now(),
                InstantUtils.now()
        );

        // when
        final var actualOutput = genreGateway.save(expectedGenre);

        // then
        Assertions.assertEquals(expectedGenre, actualOutput);

        final var actualGenre = genreRepository.findById(expectedGenre.id())
                .orElseThrow();
        Assertions.assertEquals(expectedGenre.id(), actualGenre.id());
        Assertions.assertEquals(expectedGenre.name(), actualGenre.name());
        Assertions.assertEquals(expectedGenre.active(), actualGenre.active());
        Assertions.assertEquals(expectedGenre.categories(), actualGenre.categories());
        Assertions.assertEquals(expectedGenre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(expectedGenre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(expectedGenre.deletedAt(), actualGenre.deletedAt());
    }

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var expectedGenre = Fixture.Genres.business();

        genreRepository.save(GenreDocument.from(expectedGenre));

        final var expectedId = expectedGenre.id();
        Assertions.assertTrue(genreRepository.existsById(expectedId));

        // when
        genreGateway.deleteById(expectedId);

        // then
        Assertions.assertFalse(genreRepository.existsById(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "qualquerId";

        // when
        // then
        Assertions.assertDoesNotThrow(() -> genreGateway.deleteById(expectedId));
    }

    @Test
    public void givenActiveGenreWithGenres_whenCallsFindById_shouldRetrieveIt() {
        // given
        final var expectedGenre = Genre.with(
                IdUtils.uniqueId(),
                "Business",
                true,
                Set.of("c1", "c2"),
                InstantUtils.now(),
                InstantUtils.now(),
                null
        );

        genreRepository.save(GenreDocument.from(expectedGenre));

        final var expectedId = expectedGenre.id();
        final var actualOutput = genreRepository.findById(expectedId)
                .orElseThrow();

        // when
        genreGateway.findById(expectedId);

        // then
        Assertions.assertEquals(expectedGenre.id(), actualOutput.id());
        Assertions.assertEquals(expectedGenre.name(), actualOutput.name());
        Assertions.assertEquals(expectedGenre.active(), actualOutput.active());
        Assertions.assertEquals(expectedGenre.categories(), actualOutput.categories());
        Assertions.assertEquals(expectedGenre.createdAt(), actualOutput.createdAt());
        Assertions.assertEquals(expectedGenre.updatedAt(), actualOutput.updatedAt());
        Assertions.assertEquals(expectedGenre.deletedAt(), actualOutput.deletedAt());
    }

    @Test
    public void givenInactiveGenreWithoutGenres_whenCallsFindById_shouldRetrieveIt() {
        // given
        final var expectedGenre = Genre.with(
                IdUtils.uniqueId(),
                "Business",
                false,
                new HashSet<>(),
                InstantUtils.now(),
                InstantUtils.now(),
                InstantUtils.now()
        );

        genreRepository.save(GenreDocument.from(expectedGenre));

        final var expectedId = expectedGenre.id();
        final var actualOutput = genreRepository.findById(expectedId)
                .orElseThrow();

        // when
        genreGateway.findById(expectedId);

        // then
        Assertions.assertEquals(expectedGenre.id(), actualOutput.id());
        Assertions.assertEquals(expectedGenre.name(), actualOutput.name());
        Assertions.assertEquals(expectedGenre.active(), actualOutput.active());
        Assertions.assertEquals(expectedGenre.categories(), actualOutput.categories());
        Assertions.assertEquals(expectedGenre.createdAt(), actualOutput.createdAt());
        Assertions.assertEquals(expectedGenre.updatedAt(), actualOutput.updatedAt());
        Assertions.assertEquals(expectedGenre.deletedAt(), actualOutput.deletedAt());
    }

    @Test
    public void givenValidId_whenCallsFindById_shouldReturnEmpy() {
        // given
        final var expectedId = "qualquerId";

        // when
        // then
        Assertions.assertTrue(genreGateway.findById(expectedId).isEmpty());
    }

    @Test
    public void givenEmptyGenres_whenCallsFindAll_shouldReturnEmpyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algooooo";
        final var expectedSort = "name";
        final var expectedCategories = Set.<String>of();
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        // when
        final var actualOutput = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedTotal, actualOutput.data().size()
        );
    }

    @ParameterizedTest
    @CsvSource({
            "mark,0,10,1,1,Marketing",
            "tec,0,10,1,1,Technology"
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
        final var expectedCategories = Set.<String>of();

        mockGenres();

        final var aQuery =
                new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        // when
        final var actualOutput = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "c1,0,10,1,1,Technology",
            ",0,10,3,3,Business"
    })
    public void givenValidCategories_whenCallsFindAll_shouldReturnElementsFiltered(
            final String expectedCategory,
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
        final var expectedCategories = expectedCategory == null ? Set.<String>of() : Set.of(expectedCategory);

        mockGenres();

        final var aQuery =
                new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        // when
        final var actualOutput = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,3,3,Business",
            "name,desc,0,10,3,3,Technology",
            "created_at,asc,0,10,3,3,Technology",
            "created_at,desc,0,10,3,3,Marketing",
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
        final var expectedCategories = Set.<String>of();

        mockGenres();

        final var aQuery =
                new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        // when
        final var actualOutput = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,1,1,3,Business",
            "1,1,1,3,Marketing",
            "2,1,1,3,Technology",
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
        final var expectedCategories = Set.<String>of();

        mockGenres();

        final var aQuery =
                new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        // when
        final var actualOutput = genreGateway.findAll(aQuery);

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
        final var tech = genreRepository.save(GenreDocument.from(Fixture.Genres.tech()));
        genreRepository.save(GenreDocument.from(Fixture.Genres.business()));
        final var marketing = genreRepository.save(GenreDocument.from(Fixture.Genres.marketing()));

        final var expectedSize = 2;
        final var expectedIds = Set.of(tech.id(), marketing.id());

        // when
        final var actualOutput = genreGateway.findAllById(expectedIds);

        // then
        Assertions.assertEquals(expectedSize, actualOutput.size());

        final var actualIds = actualOutput.stream().map(Genre::id).toList();
        Assertions.assertTrue(expectedIds.containsAll(actualIds));
    }

    @Test
    public void givenNullIds_whenCallsFindAllByIds_shouldReturnEmpty() {
        // given
        final Set<String> expectedIds = null;

        // when
        final var actualOutput = genreGateway.findAllById(expectedIds);

        // then
        Assertions.assertTrue(actualOutput.isEmpty());
    }

    @Test
    public void givenEmptyIds_whenCallsFindAllByIds_shouldReturnEmpty() {
        // given
        final Set<String> expectedIds = Collections.emptySet();

        // when
        final var actualOutput = genreGateway.findAllById(expectedIds);

        // then
        Assertions.assertTrue(actualOutput.isEmpty());
    }

    private void mockGenres() {
        genreRepository.save(GenreDocument.from(Fixture.Genres.tech()));
        genreRepository.save(GenreDocument.from(Fixture.Genres.business()));
        genreRepository.save(GenreDocument.from(Fixture.Genres.marketing()));
    }

}