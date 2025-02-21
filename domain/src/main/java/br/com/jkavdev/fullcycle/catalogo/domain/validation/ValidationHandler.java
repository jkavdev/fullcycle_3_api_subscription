package br.com.jkavdev.fullcycle.catalogo.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler anHandler);

    <T> T validate(Validation<T> aValidation);

    List<Error> getErrors();

    default Error firstError() {
        return hasError() ? getErrors().get(0) : null;
    }

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }


    interface Validation<T> {
        T validate();
    }
}
