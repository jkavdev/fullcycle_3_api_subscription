package br.com.jkavdev.fullcycle.subscription.domain.validation.handler;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.validation.ValidationErr;
import br.com.jkavdev.fullcycle.subscription.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final ValidationErr anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(final ValidationHandler anHandler) {
        throw DomainException.with(anHandler.getErrors());
    }

    @Override
    public <T> T validate(final Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch (final Exception ex) {
            throw DomainException.with(new ValidationErr(ex.getMessage()));
        }
    }

    @Override
    public List<ValidationErr> getErrors() {
        return List.of();
    }
}
