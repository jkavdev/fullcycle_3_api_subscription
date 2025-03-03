package br.com.jkavdev.fullcycle.subscription.infrastructure.configuration;

import br.com.jkavdev.fullcycle.subscription.domain.validation.ValidationErr;
import br.com.jkavdev.fullcycle.subscription.domain.validation.handler.Notification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return ResponseEntity.unprocessableEntity()
                .body(Notification.create(convertError(ex.getBindingResult().getAllErrors())));
    }

    private List<ValidationErr> convertError(final List<ObjectError> allErrors) {
        return allErrors.stream()
                .map(e -> new ValidationErr(((FieldError) e).getField(), e.getDefaultMessage()))
                .toList();
    }
}
