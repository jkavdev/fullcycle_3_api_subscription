package br.com.jkavdev.fullcycle.subscription;

import br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository.AccountJdbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.jdbc.JdbcTestUtils;

@DataJdbcTest
@Tag("integrationTests")
public abstract class AbstractRepositoryTest extends AbstractTest {

    private static final String ACCOUNTS_TABLE = "accounts";

    @Autowired
    private JdbcClient jdbcClient;

    private AccountJdbcRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository = new AccountJdbcRepository(jdbcClient);
    }

    protected int countAccounts() {
        return JdbcTestUtils.countRowsInTable(jdbcClient, ACCOUNTS_TABLE);
    }

    protected AccountJdbcRepository accountRepository() {
        return accountRepository;
    }

}
