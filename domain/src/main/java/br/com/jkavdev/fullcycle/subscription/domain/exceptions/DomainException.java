package br.com.jkavdev.fullcycle.subscription.domain.exceptions;

import br.com.jkavdev.fullcycle.subscription.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStacktraceException {

    protected final List<Error> errors;

    protected DomainException(final String aMessage, final List<Error> errors) {
        super(aMessage);
        this.errors = errors;
    }

    public static DomainException with(final String aMessage) {
        return new DomainException(aMessage, List.of(new Error(aMessage)));
    }

    public static DomainException with(final List<Error> errors) {
        return new DomainException("", errors);
    }

    public static DomainException with(final Error anError) {
        return new DomainException(anError.message(), List.of(anError));
    }

    public List<Error> getErrors() {
        return errors;
    }
}
