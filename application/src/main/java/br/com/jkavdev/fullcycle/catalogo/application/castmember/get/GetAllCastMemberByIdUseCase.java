package br.com.jkavdev.fullcycle.catalogo.application.castmember.get;

import br.com.jkavdev.fullcycle.catalogo.application.UseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMember;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberType;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GetAllCastMemberByIdUseCase extends UseCase<GetAllCastMemberByIdUseCase.Input, List<GetAllCastMemberByIdUseCase.Output>> {

    private final CastMemberGateway castMemberGateway;

    public GetAllCastMemberByIdUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public List<Output> execute(Input input) {
        if (input.ids().isEmpty()) {
            return Collections.emptyList();
        }
        return castMemberGateway.findAllById(input.ids()).stream()
                .map(Output::new)
                .toList();
    }

    public record Input(Set<String> ids) {
        @Override
        public Set<String> ids() {
            return ids != null ? ids : Collections.emptySet();
        }
    }

    public record Output(
            String id,
            String name,
            CastMemberType type,
            Instant createdAt,
            Instant updatedAt
    ) {

        public Output(final CastMember aMember) {
            this(
                    aMember.id(),
                    aMember.name(),
                    aMember.type(),
                    aMember.createdAt(),
                    aMember.updatedAt()
            );
        }
    }

}
