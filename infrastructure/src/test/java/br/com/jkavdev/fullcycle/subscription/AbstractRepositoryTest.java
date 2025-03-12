package br.com.jkavdev.fullcycle.subscription;

import br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository.AccountJdbcRepository;
import br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository.EventJdbcRepository;
import br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository.PlanJdbcRepository;
import br.com.jkavdev.fullcycle.subscription.infrastructure.jdbc.JdbcClientAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.jdbc.JdbcTestUtils;

@DataJdbcTest
@Tag("integrationTests")
public abstract class AbstractRepositoryTest extends AbstractTest {

    private static final String ACCOUNT_TABLE = "accounts";
    private static final String PLAN_TABLE = "plans";

    @Autowired
    private JdbcClient jdbcClient;

    private AccountJdbcRepository accountRepository;

    private EventJdbcRepository eventRepository;

    private PlanJdbcRepository planRepository;

    @BeforeEach
    void setUp() {
        eventRepository = new EventJdbcRepository(new JdbcClientAdapter(jdbcClient));
        accountRepository = new AccountJdbcRepository(new JdbcClientAdapter(jdbcClient), eventRepository);
        planRepository = new PlanJdbcRepository(new JdbcClientAdapter(jdbcClient));
    }

    protected int countAccounts() {
        return JdbcTestUtils.countRowsInTable(jdbcClient, ACCOUNT_TABLE);
    }

    protected AccountJdbcRepository accountRepository() {
        return accountRepository;
    }

    protected EventJdbcRepository eventRepository() {
        return eventRepository;
    }

    protected int countPlans() {
        return JdbcTestUtils.countRowsInTable(jdbcClient, PLAN_TABLE);
    }

    protected PlanJdbcRepository planRepository() {
        return planRepository;
    }
}
