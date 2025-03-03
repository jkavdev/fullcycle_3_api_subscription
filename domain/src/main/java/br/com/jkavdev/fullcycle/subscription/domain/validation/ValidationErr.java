package br.com.jkavdev.fullcycle.subscription.domain.validation;

public record ValidationErr(
        String property,
        String message
) {
    public ValidationErr(final String message) {
        this(message, "");
    }
}