package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.AbstractRepositoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AccountJdbcRepositoryTest extends AbstractRepositoryTest {

    @Test
    void testAssertDependencies() {
        Assertions.assertNotNull(accountRepository());
    }

}