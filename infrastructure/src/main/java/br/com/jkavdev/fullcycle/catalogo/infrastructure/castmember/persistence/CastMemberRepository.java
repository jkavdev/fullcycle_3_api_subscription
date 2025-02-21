package br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.persistence;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CastMemberRepository extends ElasticsearchRepository<CastMemberDocument, String> {
}
