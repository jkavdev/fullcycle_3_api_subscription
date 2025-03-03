package br.com.jkavdev.fullcycle.subscription.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(ValidationErr anError);

    ValidationHandler append(ValidationHandler anHandler);

    <T> T validate(Validation<T> aValidation);

    List<ValidationErr> getErrors();

    default ValidationErr firstError() {
        return hasError() ? getErrors().get(0) : null;
    }

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }


    interface Validation<T> {
        T validate();
    }
}
