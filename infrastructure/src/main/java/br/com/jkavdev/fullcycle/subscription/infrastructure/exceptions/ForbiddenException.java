package br.com.jkavdev.fullcycle.subscription.infrastructure.exceptions;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.NoStacktraceException;

public class ForbiddenException extends NoStacktraceException {

    protected ForbiddenException(final String aMessage, final Throwable t) {
        super(aMessage, t);
    }

    public static ForbiddenException with(final String message) {
        return new ForbiddenException(message, null);
    }

}
