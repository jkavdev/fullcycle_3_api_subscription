package br.com.jkavdev.fullcycle.subscription;

import br.com.jkavdev.fullcycle.subscription.infrastructure.authentication.principal.CodeflixUser;
import br.com.jkavdev.fullcycle.subscription.infrastructure.authentication.principal.CodelixAuthentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;

public interface ApiTest {

    static RequestPostProcessor admin(final String accountId) {
        Jwt.Builder jwtBuilder = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim(JwtClaimNames.SUB, "user")
                .claim("scope", "read");

        return SecurityMockMvcRequestPostProcessors.authentication(
                new CodelixAuthentication(
                        jwtBuilder.build(),
                        new CodeflixUser("test user", "keycloak-123", accountId),
                        List.of((new SimpleGrantedAuthority("ROLE_SUBSCRIPTION_ADMIN"))
                        )));
    }

    static RequestPostProcessor admin() {
        return admin("123");
    }

}
