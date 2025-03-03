package br.com.jkavdev.fullcycle.subscription.domain.validation.handler;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.validation.ValidationErr;
import br.com.jkavdev.fullcycle.subscription.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<ValidationErr> errors;

    private Notification(final List<ValidationErr> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Throwable t) {
        return create(new ValidationErr(t.getMessage()));
    }

    public static Notification create(final ValidationErr anError) {
        return new Notification(new ArrayList<>()).append(anError);
    }

    public static Notification create(final List<ValidationErr> errors) {
        return new Notification(new ArrayList<>(errors));
    }

    @Override
    public Notification append(final ValidationErr anError) {
        this.errors.add(anError);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler anHandler) {
        this.errors.addAll(anHandler.getErrors());
        return this;
    }

    @Override
    public <T> T validate(final Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch (DomainException ex) {
            this.errors.addAll(ex.getErrors());
        } catch (Throwable ex) {
            this.errors.add(new ValidationErr(ex.getMessage()));
        }
        return null;
    }

    @Override
    public List<ValidationErr> getErrors() {
        return this.errors;
    }
}
