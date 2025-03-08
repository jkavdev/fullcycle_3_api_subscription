package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.AbstractRepositoryTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;
import br.com.jkavdev.fullcycle.subscription.domain.person.Address;
import br.com.jkavdev.fullcycle.subscription.domain.person.Document;
import br.com.jkavdev.fullcycle.subscription.domain.person.Email;
import br.com.jkavdev.fullcycle.subscription.domain.person.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;

class AccountJdbcRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private JdbcClient jdbcClient;

    @Test
    void testAssertDependencies() {
        Assertions.assertNotNull(accountRepository());
    }

    @Test
    @Sql({"classpath:/sql/accounts/seed-account-jhou.sql"})
    public void givenPersistedAccount_whenQueriesSuccessfully_shouldReturnIt() {
        // given
        Assertions.assertEquals(1, countAccounts());

        final var expectedId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        final var expectedVersion = 0;
        final var expectedUserId = new UserId("a8b3cf5a-5f81-4822-9ee8-89e768f6095c");
        final var expectedName = new Name("Jhou", "JK");
        final var expectedEmail = new Email("jhou@gmail.com");
        final var expectedDocument = Document.create("12312312332", "cpf");
        final var expectedAddress = new Address("12332123", "1", "Casa 1", "BR");

        // when
        final var actualAccount = accountRepository().accountOfId(expectedId).orElseThrow();

        // then
        Assertions.assertEquals(expectedId, actualAccount.id());
        Assertions.assertEquals(expectedVersion, actualAccount.version());
        Assertions.assertEquals(expectedUserId, actualAccount.userId());
        Assertions.assertEquals(expectedEmail, actualAccount.email());
        Assertions.assertEquals(expectedName, actualAccount.name());
        Assertions.assertEquals(expectedDocument, actualAccount.document());
        Assertions.assertEquals(expectedAddress, actualAccount.billingAddress());
    }

}