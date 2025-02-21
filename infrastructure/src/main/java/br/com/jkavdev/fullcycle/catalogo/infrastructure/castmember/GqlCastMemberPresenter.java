package br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember;

import br.com.jkavdev.fullcycle.catalogo.application.castmember.get.GetAllCastMemberByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.castmember.list.ListCastMemberOutput;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMember;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.models.GqlCastMember;

public final class GqlCastMemberPresenter {

    private GqlCastMemberPresenter() {

    }

    public static GqlCastMember present(final ListCastMemberOutput out) {
        return new GqlCastMember(
                out.id(),
                out.name(),
                out.type().name(),
                out.createdAt().toString(),
                out.updatedAt().toString()
        );
    }

    public static GqlCastMember present(final GetAllCastMemberByIdUseCase.Output out) {
        return new GqlCastMember(
                out.id(),
                out.name(),
                out.type().name(),
                out.createdAt().toString(),
                out.updatedAt().toString()
        );
    }

    public static GqlCastMember present(final CastMember aMember) {
        return new GqlCastMember(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString(),
                aMember.updatedAt().toString()
        );
    }

}
