package br.com.jkavdev.fullcycle.catalogo.infrastructure.video.persistence;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface VideoRepository extends ElasticsearchRepository<VideoDocument, String> {
}
