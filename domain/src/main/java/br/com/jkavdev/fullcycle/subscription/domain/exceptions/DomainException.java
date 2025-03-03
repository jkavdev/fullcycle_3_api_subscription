package br.com.jkavdev.fullcycle.subscription.domain.exceptions;

import br.com.jkavdev.fullcycle.subscription.domain.AggregateRoot;
import br.com.jkavdev.fullcycle.subscription.domain.Identifier;
import br.com.jkavdev.fullcycle.subscription.domain.validation.ValidationErr;

import java.util.List;

public class DomainException extends NoStacktraceException {

    protected final List<ValidationErr> errors;

    protected DomainException(final String aMessage, final List<ValidationErr> errors) {
        super(aMessage);
        this.errors = errors;
    }

    public static DomainException with(final String aMessage) {
        return new DomainException(aMessage, List.of(new ValidationErr(aMessage)));
    }

    public static DomainException with(final List<ValidationErr> errors) {
        return new DomainException("", errors);
    }

    public static DomainException with(final ValidationErr anError) {
        return new DomainException(anError.message(), List.of(anError));
    }

    public static RuntimeException notFound(Class<? extends AggregateRoot<?>> aggClass, Identifier identifier) {
        return DomainException.with(
                "%s with id %s was not found".formatted(aggClass.getCanonicalName(), identifier.value())
        );
    }

    public List<ValidationErr> getErrors() {
        return errors;
    }
}
