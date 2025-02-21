package br.com.jkavdev.fullcycle.catalogo.infrastructure.video;

import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Video;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoSearchQuery;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("development")
public class VideoInMemoryGateway implements VideoGateway {

    private final ConcurrentHashMap<String, Video> db;

    public VideoInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public Video save(Video aGenre) {
        this.db.put(aGenre.id(), aGenre);
        return aGenre;
    }

    @Override
    public void deleteById(String anId) {
        db.remove(anId);
    }

    @Override
    public Optional<Video> findById(String anId) {
        return Optional.ofNullable(db.get(anId));
    }

    @Override
    public Pagination<Video> findAll(VideoSearchQuery aQuery) {
        final var values = db.values();
        return new Pagination<>(aQuery.page(), aQuery.perPage(), values.size(), new ArrayList<>(values));
    }

}
