package br.com.jkavdev.fullcycle.subscription.domain.exceptions;

public class InternalErrorException extends NoStacktraceException {

    protected InternalErrorException(final String aMessage, final Throwable t) {
        super(aMessage, t);
    }

    public static InternalErrorException with(final String message, final Throwable t) {
        return new InternalErrorException(message, t);
    }

    public static InternalErrorException with(final String message) {
        return new InternalErrorException(message, null);
    }

}
