package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.AbstractRepositoryTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.Account;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountEvent;
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
        final var expectedVersion = 1;
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

    @Test
    public void givenEmptyTable_whenPersistSuccessfully_shouldHaveUserAtTheEndOfOperation() {
        // given
        Assertions.assertEquals(0, countAccounts());

        final var expectedId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        final var expectedVersion = 1;
        final var expectedUserId = new UserId("a8b3cf5a-5f81-4822-9ee8-89e768f6095c");
        final var expectedName = new Name("Jhou", "JK");
        final var expectedEmail = new Email("jhou@gmail.com");
        final var expectedDocument = Document.create("12312312332", "cpf");

        final var anAccount = Account.newAccount(
                expectedId,
                expectedUserId,
                expectedEmail,
                expectedName,
                expectedDocument
        );

        // when
        final var actualResponse = accountRepository().save(anAccount);
        Assertions.assertEquals(expectedId, actualResponse.id());
        Assertions.assertEquals(0, actualResponse.version());
        Assertions.assertEquals(expectedUserId, actualResponse.userId());
        Assertions.assertEquals(expectedEmail, actualResponse.email());
        Assertions.assertEquals(expectedName, actualResponse.name());
        Assertions.assertEquals(expectedDocument, actualResponse.document());
        Assertions.assertNull(actualResponse.billingAddress());

        // then
        Assertions.assertEquals(1, countAccounts());

        final var actualAccount = accountRepository().accountOfId(expectedId).orElseThrow();
        Assertions.assertEquals(expectedId, actualAccount.id());
        Assertions.assertEquals(expectedVersion, actualAccount.version());
        Assertions.assertEquals(expectedUserId, actualAccount.userId());
        Assertions.assertEquals(expectedEmail, actualAccount.email());
        Assertions.assertEquals(expectedName, actualAccount.name());
        Assertions.assertEquals(expectedDocument, actualAccount.document());
        Assertions.assertNull(actualAccount.billingAddress());

        final var actualEvents = eventRepository().allEventsOfAggregate(expectedId.value(), AccountEvent.TYPE);
        Assertions.assertEquals(1, actualEvents.size());

        final var actualEvent = (AccountEvent.AccountCreated) actualEvents.getFirst();
        Assertions.assertEquals(expectedId.value(), actualEvent.accountId());
        Assertions.assertEquals(expectedEmail.value(), actualEvent.email());
        Assertions.assertEquals(expectedName.fullname(), actualEvent.fullname());
        Assertions.assertNotNull(actualEvent.occurredOn());

    }

    @Test
    @Sql({"classpath:/sql/accounts/seed-account-jhou.sql"})
    public void givenJhouPersisted_whenUpdateSuccessfully_shouldHaveChangeUserAtTheEndOfOperation() {
        // given
        Assertions.assertEquals(1, countAccounts());

        final var expectedId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        final var expectedVersion = 2;
        final var expectedUserId = new UserId("a8b3cqqq-5f81-4822-9ee8-89e768f6095c");
        final var expectedName = new Name("Jhous", "JKs");
        final var expectedEmail = new Email("jhous@gmail.com");
        final var expectedDocument = Document.create("42312312332", "cpf");
        final var expectedAddress = new Address("12332123s", "2", "Casa 2", "BR");

        final var anAccount = Account.with(
                expectedId,
                1,
                expectedUserId,
                expectedEmail,
                expectedName,
                expectedDocument,
                expectedAddress
        );

        // when
        final var actualResponse = accountRepository().save(anAccount);
        Assertions.assertEquals(expectedId, actualResponse.id());
        Assertions.assertEquals(1, actualResponse.version());
        Assertions.assertEquals(expectedUserId, actualResponse.userId());
        Assertions.assertEquals(expectedEmail, actualResponse.email());
        Assertions.assertEquals(expectedName, actualResponse.name());
        Assertions.assertEquals(expectedDocument, actualResponse.document());
        Assertions.assertEquals(expectedAddress, actualResponse.billingAddress());

        // then
        Assertions.assertEquals(1, countAccounts());

        final var actualAccount = accountRepository().accountOfId(expectedId).orElseThrow();
        Assertions.assertEquals(expectedId, actualAccount.id());
        Assertions.assertEquals(expectedVersion, actualAccount.version());
        Assertions.assertEquals(expectedUserId, actualAccount.userId());
        Assertions.assertEquals(expectedEmail, actualAccount.email());
        Assertions.assertEquals(expectedName, actualAccount.name());
        Assertions.assertEquals(expectedDocument, actualAccount.document());
        Assertions.assertEquals(expectedAddress, actualAccount.billingAddress());

    }

    @Test
    @Sql({"classpath:/sql/accounts/seed-account-jhou.sql"})
    public void givenJhouPersisted_whenQueryByUserIdSuccessfully_shouldReturnAccount() {
        // given
        Assertions.assertEquals(1, countAccounts());

        final var expectedId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        final var expectedVersion = 1;
        final var expectedUserId = new UserId("a8b3cf5a-5f81-4822-9ee8-89e768f6095c");
        final var expectedName = new Name("Jhou", "JK");
        final var expectedEmail = new Email("jhou@gmail.com");
        final var expectedDocument = Document.create("12312312332", "cpf");

        // when
        final var actualResponse = accountRepository().accountOfUserId(expectedUserId).orElseThrow();

        // then
        Assertions.assertEquals(expectedId, actualResponse.id());
        Assertions.assertEquals(expectedVersion, actualResponse.version());
        Assertions.assertEquals(expectedUserId, actualResponse.userId());
        Assertions.assertEquals(expectedEmail, actualResponse.email());
        Assertions.assertEquals(expectedName, actualResponse.name());
        Assertions.assertEquals(expectedDocument, actualResponse.document());

    }

}