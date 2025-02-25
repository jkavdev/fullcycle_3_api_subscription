package br.com.jkavdev.fullcycle.subscription.domain.account.idp;

import br.com.jkavdev.fullcycle.subscription.domain.AssertionConcern;
import br.com.jkavdev.fullcycle.subscription.domain.person.Email;
import br.com.jkavdev.fullcycle.subscription.domain.person.Name;

public class User implements AssertionConcern {

    private final UserId userId;

    private Name name;

    private Email email;

    private boolean enabled;

    private boolean emailVerified;

    private String password;

    public User(
            final UserId userId,
            final Name name,
            final Email email,
            final Boolean enabled,
            final Boolean emailVerified
    ) {
        this.userId = userId;
        setName(name);
        setEmail(email);
        setEnabled(enabled);
        setEmailVerified(emailVerified);
    }

    public User(
            final UserId userId,
            final Name name,
            final Email email,
            final Boolean enabled,
            final Boolean emailVerified,
            final String password
    ) {
        this(userId, name, email, enabled, emailVerified);
        setPassword(this.assertArgumentNotEmpty(password, "user 'password' should not be null for new users"));
    }

    public static User newUser(
            final Name name,
            final Email email,
            final String password
    ) {
        return new User(UserId.empty(), name, email, true, false, password);
    }

    public static User with(
            final UserId id,
            final Name name,
            final Email email,
            final Boolean enabled,
            final Boolean emailVerified
    ) {
        return new User(id, name, email, enabled, emailVerified);
    }

    public UserId userId() {
        return userId;
    }

    public Name name() {
        return name;
    }

    public Email email() {
        return email;
    }

    public boolean enabled() {
        return enabled;
    }

    public boolean emailVerified() {
        return emailVerified;
    }

    public String password() {
        return password;
    }

    private void setName(final Name name) {
        this.assertArgumentNotNull(name, "user 'name' should not be null");
        this.name = name;
    }

    private void setEmail(final Email email) {
        this.assertArgumentNotNull(email, "user 'email' should not be null");
        this.email = email;
    }

    private void setEnabled(final Boolean enabled) {
        this.enabled = enabled != null ? enabled : true;
    }

    private void setEmailVerified(final Boolean emailVerified) {
        this.emailVerified = emailVerified != null ? emailVerified : false;
    }

    private void setPassword(final String password) {
        this.password = password;
    }
}
