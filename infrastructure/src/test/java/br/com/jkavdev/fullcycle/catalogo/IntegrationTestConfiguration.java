package br.com.jkavdev.fullcycle.catalogo;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.persistence.CategoryRepository;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.persistence.GenreRepository;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.persistence.VideoRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

// configurando os beans para que o teste nao fique lento, tendo que subir todo o contexto do spring
public class IntegrationTestConfiguration {

    @Bean
    public CategoryRepository categoryRepository() {
        return Mockito.mock(CategoryRepository.class);
    }

    @Bean
    public CastMemberRepository castMemberRepository() {
        return Mockito.mock(CastMemberRepository.class);
    }

    @Bean
    public GenreRepository genreRepository() {
        return Mockito.mock(GenreRepository.class);
    }

    @Bean
    public VideoRepository videoRepository() {
        return Mockito.mock(VideoRepository.class);
    }

    @Bean
    public WebGraphQLSecurityInterceptor webGraphQLSecurityInterceptor() {
        return new WebGraphQLSecurityInterceptor();
    }

}