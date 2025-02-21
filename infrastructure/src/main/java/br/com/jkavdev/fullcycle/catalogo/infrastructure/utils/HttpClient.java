package br.com.jkavdev.fullcycle.catalogo.infrastructure.utils;

import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.InternalErrorException;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.exceptions.NotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;

import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpTimeoutException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface HttpClient {

    Predicate<HttpStatusCode> isNotFound = HttpStatus.NOT_FOUND::equals;

    Predicate<HttpStatusCode> is5xx = HttpStatusCode::is5xxServerError;

    String namespace();

    default ErrorHandler notFoundHandler(final String id) {
        return (req, res) -> {
            throw NotFoundException.with("not found observed from %s [resourceId::%s]".formatted(namespace(), id));
        };
    }

    default ErrorHandler a5xxHandler(final String id) {
        return (req, res) -> {
            throw InternalErrorException.with("error observed from %s [resourceId::%s] [status::%s]".
                    formatted(namespace(), id, res.getStatusCode().value()));
        };
    }

    default <T> Optional<T> doGet(final String id, final Supplier<T> fn) {
        try {
            return Optional.ofNullable(fn.get());
        } catch (NotFoundException ex) {
            return Optional.empty();
        } catch (ResourceAccessException ex) {
            throw handleResourceAccessException(id, ex);
        } catch (Throwable e) {
            throw handleUnexpectedException(id, e);
        }
    }

    private InternalErrorException handleResourceAccessException(final String id, final ResourceAccessException ex) {
        final var cause = ExceptionUtils.getRootCause(ex);
        if (cause instanceof HttpConnectTimeoutException) {
            return InternalErrorException.with("connectTimeout observed from %s [resourceId::%s]".formatted(namespace(), id));
        }
        if (cause instanceof HttpTimeoutException || cause instanceof TimeoutException) {
            return InternalErrorException.with("timeout observed from %s [resourceId::%s]".formatted(namespace(), id));
        }
        return InternalErrorException.with("error observed from %s [resourceId::%s]".formatted(namespace(), id));
    }

    private InternalErrorException handleUnexpectedException(final String id, final Throwable t) {
        // se ja tiver sido tratada, apenas retorna o erro interno tratado
        if (t instanceof InternalErrorException ex) {
            return ex;
        }
        // caso um erro generico, encapsula no erro interno
        return InternalErrorException.with("unhandled error observed from %s [resourceId::%s]".formatted(namespace(), id));
    }

}
