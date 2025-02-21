package br.com.jkavdev.fullcycle.catalogo.application.castmember.delete;

import br.com.jkavdev.fullcycle.catalogo.application.UnitUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberGateway;

import java.util.Objects;

public class DeleteCastMemberUseCase extends UnitUseCase<String> {

    private final CastMemberGateway castMemberGateway;

    public DeleteCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public void execute(final String anId) {
        if (anId == null) {
            return;
        }
        castMemberGateway.deleteById(anId);
    }
}
