package br.com.jkavdev.fullcycle.catalogo;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebGraphQLSecurityInterceptor implements WebGraphQlInterceptor {

    private final List<SimpleGrantedAuthority> authorities;

    public WebGraphQLSecurityInterceptor() {
        this.authorities = new ArrayList<>();
    }

    public void setAuthorities(final String... authorities) {
        this.authorities.clear();
        if (authorities != null) {
            this.authorities.addAll(Arrays.stream(authorities).map(SimpleGrantedAuthority::new).toList());
        }
    }

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        // se vazio, entao segue requisicao sem aplicar o usuario autenticado mocado
        if (authorities.isEmpty()) {
            return chain.next(request);
        }

        // criando um usuario mocado com as authorities
        final var user =
                UsernamePasswordAuthenticationToken.authenticated("qualquerUm", "qualquerUma", authorities);
        final var context = SecurityContextHolder.getContext();
        context.setAuthentication(user);

        // atribuindo o usuario mocado ao contexto do spring
        return chain.next(request)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
    }
}
