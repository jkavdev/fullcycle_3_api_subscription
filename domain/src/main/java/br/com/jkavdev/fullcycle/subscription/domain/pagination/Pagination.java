package br.com.jkavdev.fullcycle.subscription.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Pagination<T>(
        Metadata metadata,
        List<T> data
) {

    public Pagination(
            final int currentPage,
            final int perPage,
            final long total,
            final List<T> data
    ) {
        this(new Metadata(currentPage, perPage, total), data);
    }

    public <R> Pagination<R> map(final Function<T, R> mapper) {
        final List<R> aNewList = data.stream()
                .map(mapper)
                .toList();
        return new Pagination<>(metadata(), aNewList);
    }
}
