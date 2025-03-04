package br.com.jkavdev.fullcycle.subscription;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

public interface ApiTest {

    static JwtRequestPostProcessor admin() {
        return SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority("ROLE_SUBSCRIPTION_ADMIN"));
    }

}
