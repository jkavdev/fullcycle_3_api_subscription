package br.com.jkavdev.fullcycle.catalogo.application.castmember.save;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMember;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberType;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.UUID;

public class SaveCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private SaveCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenValidCastMember_whenCallsSave_shouldPersistIt() {
        // given
        final var aMember = Fixture.CastMembers.gabriel();

        Mockito.when(castMemberGateway.save(ArgumentMatchers.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        useCase.execute(aMember);

        // then
        Mockito.verify(castMemberGateway, Mockito.times(1))
                .save(aMember);

    }

    @Test
    public void givenNullCastMember_whenCallsSave_shouldReturnError() {
        // given
        final CastMember aMember = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'aMember' cannot be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(aMember));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, Mockito.never())
                .save(aMember);

    }

    @Test
    public void givenInvalidNameCastMember_whenCallsSave_shouldReturnError() {
        // given
        final var aMember = CastMember.with(
                UUID.randomUUID().toString().replace("-", ""),
                null,
                CastMemberType.ACTOR,
                InstantUtils.now(),
                InstantUtils.now()
        );
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(aMember));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, Mockito.never())
                .save(aMember);

    }

    @Test
    public void givenInvalidIdCastMember_whenCallsSave_shouldReturnError() {
        // given
        final var aMember = CastMember.with(
                null,
                "Aula",
                CastMemberType.ACTOR,
                InstantUtils.now(),
                InstantUtils.now()
        );
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(aMember));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, Mockito.never())
                .save(aMember);

    }

    @Test
    public void givenInvalidNullTypeCastMember_whenCallsSave_shouldReturnError() {
        // given
        final var aMember = CastMember.with(
                UUID.randomUUID().toString().replace("-", ""),
                "Aula",
                null,
                InstantUtils.now(),
                InstantUtils.now()
        );
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(aMember));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, Mockito.never())
                .save(aMember);

    }

}
