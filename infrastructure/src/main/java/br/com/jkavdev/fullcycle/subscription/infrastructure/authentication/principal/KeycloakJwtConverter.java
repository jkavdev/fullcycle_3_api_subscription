package br.com.jkavdev.fullcycle.subscription.infrastructure.authentication.principal;

import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class KeycloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final KeycloakAuthoritiesConverter authoritiesConverter;

    private final AccountGateway accountGateway;

    public KeycloakJwtConverter(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        authoritiesConverter = new KeycloakAuthoritiesConverter();
    }

    @Override
    public AbstractAuthenticationToken convert(final Jwt jwt) {
        return new CodelixAuthentication(jwt, extractPrincipal(jwt), extractAuthorities(jwt));
    }

    private CodeflixUser extractPrincipal(final Jwt jwt) {
        return CodeflixUser.fromJwt(jwt, accountGateway::accountOfUserId);
    }

    private Collection<? extends GrantedAuthority> extractAuthorities(final Jwt jwt) {
        return this.authoritiesConverter.convert(jwt);
    }

    static class KeycloakAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        private static final String REALM_ACCESS = "realm_access";
        private static final String ROLES = "roles";
        private static final String RESOURCE_ACCESS = "resource_access";
        private static final String SEPARATOR = "_";
        private static final String ROLE_PREFIX = "ROLE_"; // padrao do spring ter role_ como prefixo

        @Override
        public Collection<GrantedAuthority> convert(final Jwt jwt) {
//            extraindo as roles de realms e clientes
            final var realmRoles = extractRealmRoles(jwt);
            final var resourceRoles = extractResourceRoles(jwt);

            return Stream.concat(realmRoles, resourceRoles)
                    .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.toUpperCase()))
                    .collect(Collectors.toSet());
        }

        private Stream<String> extractResourceRoles(final Jwt jwt) {

            final Function<Map.Entry<String, Object>, Stream<String>> mapResource =
                    resource -> {
                        final var key = resource.getKey();
                        final var value = (Map) resource.getValue();
                        final var roles = (Collection<String>) value.get(ROLES);
                        return roles.stream().map(role -> key.concat(SEPARATOR).concat(role));
                    };

            final Function<Set<Map.Entry<String, Object>>, Collection<String>> mapResources =
                    resources -> resources.stream()
                            .flatMap(mapResource)
                            .toList();

            return Optional.ofNullable(jwt.getClaimAsMap(RESOURCE_ACCESS))
                    .map(resources -> resources.entrySet())
                    .map(mapResources)
                    .orElse(Collections.emptyList())
                    .stream();
        }

        private Stream<String> extractRealmRoles(final Jwt jwt) {
            return Optional.ofNullable(jwt.getClaimAsMap(REALM_ACCESS))
                    .map(resource -> (Collection<String>) resource.get(ROLES))
                    .orElse(Collections.emptyList())
                    .stream();
        }
    }

}