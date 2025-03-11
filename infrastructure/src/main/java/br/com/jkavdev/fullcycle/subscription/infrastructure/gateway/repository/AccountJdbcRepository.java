package br.com.jkavdev.fullcycle.subscription.infrastructure.gateway.repository;

import br.com.jkavdev.fullcycle.subscription.domain.account.Account;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;
import br.com.jkavdev.fullcycle.subscription.domain.person.Address;
import br.com.jkavdev.fullcycle.subscription.domain.person.Document;
import br.com.jkavdev.fullcycle.subscription.domain.person.Email;
import br.com.jkavdev.fullcycle.subscription.domain.person.Name;
import br.com.jkavdev.fullcycle.subscription.domain.utils.IdUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@Repository
public class AccountJdbcRepository implements AccountGateway {

    private final JdbcClient jdbcClient;

    private final EventJdbcRepository eventJdbcRepository;

    public AccountJdbcRepository(
            final JdbcClient jdbcClient,
            final EventJdbcRepository eventJdbcRepository
    ) {
        this.jdbcClient = Objects.requireNonNull(jdbcClient);
        this.eventJdbcRepository = Objects.requireNonNull(eventJdbcRepository);
    }

    @Override
    public AccountId nextId() {
        return new AccountId(IdUtils.uniqueId());
    }

    @Override
    public Optional<Account> accountOfId(AccountId anId) {
        final var sql = """
                SELECT
                    id,
                    version,
                    idp_user_id,
                    email,
                    firstname,
                    lastname,
                    document_number,
                    document_type,
                    address_zip_code,
                    address_number,
                    address_complement,
                    address_country
                FROM accounts
                WHERE id = :id
                """;
        return jdbcClient.sql(sql)
                .param("id", anId.value())
                .query(accountMapper())
                .optional();
    }

    private RowMapper<Account> accountMapper() {
        return (rs, rowNumber) -> {
            String zipCode = rs.getString("address_zip_code");
            return Account.with(
                    new AccountId(rs.getString("id")),
                    rs.getInt("version"),
                    new UserId(rs.getString("idp_user_id")),
                    new Email(rs.getString("email")),
                    new Name(rs.getString("firstname"), rs.getString("lastname")),
                    Document.create(rs.getString("document_number"), rs.getString("document_type")),
                    zipCode != null && !zipCode.isBlank()
                            ? new Address(
                            zipCode,
                            rs.getString("address_number"),
                            rs.getString("address_complement"),
                            rs.getString("address_country")
                    )
                            : null
            );
        };
    }

    @Override
    public Optional<Account> accountOfUserId(UserId userId) {
        return Optional.empty();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Account save(final Account anAccount) {
        if (anAccount.version() == 0) {
            create(anAccount);
        } else {
            update(anAccount);
        }

        eventJdbcRepository.saveAll(anAccount.domainEvents());

        return anAccount;
    }

    private void create(final Account anAccount) {
        final var sql = """
                INSERT INTO accounts(
                    id,
                    version,
                    idp_user_id,
                    email,
                    firstname,
                    lastname,
                    document_number,
                    document_type,
                    address_zip_code,
                    address_number,
                    address_complement,
                    address_country)
                values(
                    :id,
                    (:version + 1),
                    :idpUserId,
                    :email,
                    :firstname,
                    :lastname,
                    :documentNumber,
                    :documentType,
                    :addressZipCode,
                    :addressNumber,
                    :addressComplement,
                    :addressCountry
                )
                """;
        executeUpdate(sql, anAccount);
    }

    private void update(final Account anAccount) {
        final var sql = """
                UPDATE accounts SET
                    version = :version + 1,
                    idp_user_id = :idpUserId,
                    email = :email,
                    firstname = :firstname,
                    lastname = :lastname,
                    document_number = :documentNumber,
                    document_type = :documentType,
                    address_zip_code = :addressZipCode,
                    address_number = :addressNumber,
                    address_complement = :addressComplement,
                    address_country = :addressCountry
                WHERE id = :id AND version = :version
                """;

        if (executeUpdate(sql, anAccount) == 0) {
            throw new IllegalArgumentException(
                    "account with id %s and version %s was not fount".formatted(anAccount.id().value(), anAccount.version())
            );
        }

    }

    private int executeUpdate(final String sql, final Account account) {
        final var params = new HashMap<String, Object>();
        params.put("id", account.id().value());
        params.put("version", account.version());
        params.put("idpUserId", account.userId().value());
        params.put("email", account.email().value());
        params.put("firstname", account.name().firstname());
        params.put("lastname", account.name().lastname());
        params.put("documentNumber", account.document().value());
        params.put("documentType", account.document().type());

        final var address = account.billingAddress();
        params.put("addressZipCode", address != null ? address.zipcode() : "");
        params.put("addressNumber", address != null ? address.number() : "");
        params.put("addressComplement", address != null ? address.complement() : "");
        params.put("addressCountry", address != null ? address.country() : "");

        try {
            return jdbcClient.sql(sql).params(params).update();
        } catch (DataIntegrityViolationException exception) {
            throw exception;
        }
    }

}
