package br.com.jkavdev.fullcycle.catalogo.application.castmember.save;

import br.com.jkavdev.fullcycle.catalogo.application.UseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMember;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.NotificationException;
import br.com.jkavdev.fullcycle.catalogo.domain.validation.Error;
import br.com.jkavdev.fullcycle.catalogo.domain.validation.handler.Notification;

import java.util.Objects;

public class SaveCastMemberUseCase extends UseCase<CastMember, CastMember> {

    private final CastMemberGateway castMemberGateway;

    public SaveCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMember execute(final CastMember aMember) {
        if (aMember == null) {
            throw NotificationException.with(new Error("'aMember' cannot be null"));
        }

        final var notification = Notification.create();
        aMember.validate(notification);

        if (notification.hasError()) {
            throw NotificationException.with("invalid cast member", notification);
        }

        return castMemberGateway.save(aMember);
    }
}
