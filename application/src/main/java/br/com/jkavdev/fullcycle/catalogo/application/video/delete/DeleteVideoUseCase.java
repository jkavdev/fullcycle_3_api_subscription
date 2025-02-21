package br.com.jkavdev.fullcycle.catalogo.application.video.delete;

import br.com.jkavdev.fullcycle.catalogo.application.UnitUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoGateway;

import java.util.Objects;

public class DeleteVideoUseCase extends UnitUseCase<DeleteVideoUseCase.Input> {

    private final VideoGateway videoGateway;

    public DeleteVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final Input input) {
        if (input == null || input.id() == null) {
            return;
        }

        videoGateway.deleteById(input.id());
    }

    public record Input(String id) {

    }
}
