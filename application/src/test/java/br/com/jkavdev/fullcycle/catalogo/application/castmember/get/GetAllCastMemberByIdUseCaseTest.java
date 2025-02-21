package br.com.jkavdev.fullcycle.catalogo.application.castmember.get;

import br.com.jkavdev.fullcycle.catalogo.application.UseCaseTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMember;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class GetAllCastMemberByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetAllCastMemberByIdUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenValidQuery_whenCallsAllById_shouldReturnIt() {
        // given
        final var members = List.of(
                Fixture.CastMembers.gabriel(),
                Fixture.CastMembers.wesley()
        );

        final var expectedIds = members.stream().map(CastMember::id).collect(Collectors.toSet());

        final var expectedItems = members.stream()
                .map(GetAllCastMemberByIdUseCase.Output::new)
                .toList();

        Mockito.when(castMemberGateway.findAllById(ArgumentMatchers.any()))
                .thenReturn(members);

        // when
        final var actualOutput = useCase.execute(new GetAllCastMemberByIdUseCase.Input(expectedIds));

        // then
        Assertions.assertTrue(
                expectedItems.size() == actualOutput.size()
                        && expectedItems.containsAll(actualOutput)
        );

        Mockito.verify(castMemberGateway, Mockito.times(1)).findAllById(expectedIds);

    }

    @Test
    public void givenNullIds_whenCallsAllById_shouldReturnEmpty() {
        // given
        final Set<String> expectedIds = null;

        // when
        final var actualOutput = useCase.execute(new GetAllCastMemberByIdUseCase.Input(expectedIds));

        // then
        Assertions.assertTrue(actualOutput.isEmpty());

        Mockito.verify(castMemberGateway, Mockito.never()).findAllById(ArgumentMatchers.any());

    }

}