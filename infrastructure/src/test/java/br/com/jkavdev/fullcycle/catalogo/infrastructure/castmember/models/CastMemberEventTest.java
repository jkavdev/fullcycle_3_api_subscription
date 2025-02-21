package br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.models;

import br.com.jkavdev.fullcycle.catalogo.domain.UnitTest;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

class CastMemberEventTest extends UnitTest {

    @Test
    public void test() {
        // given
        final var expectedId = "qualquerId";
        final var expectedName = "qualquerNome";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedUnixTimestamp = 1707086611086071L;
        final var expectedDate = LocalDateTime.of(2024, 02, 04, 22, 43, 31)
                .toInstant(ZoneOffset.UTC);

        // when
        final var actualMember = new CastMemberEvent(
                expectedId, expectedName, expectedType.name(), expectedUnixTimestamp, expectedUnixTimestamp
        ).toCastMember();

        // then
        Assertions.assertEquals(expectedId, actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertEquals(expectedDate, actualMember.createdAt().truncatedTo(ChronoUnit.SECONDS));
        Assertions.assertEquals(expectedDate, actualMember.updatedAt().truncatedTo(ChronoUnit.SECONDS));
    }

}