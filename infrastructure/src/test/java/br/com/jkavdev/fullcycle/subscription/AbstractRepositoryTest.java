package br.com.jkavdev.fullcycle.subscription;

import br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository.AccountJdbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;

@DataJdbcTest
@Tag("integrationTests")
public abstract class AbstractRepositoryTest extends AbstractTest {

    @Autowired
    private JdbcClient jdbcClient;

    private AccountJdbcRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository = new AccountJdbcRepository(jdbcClient);
    }

    protected AccountJdbcRepository accountRepository() {
        return accountRepository;
    }

}
