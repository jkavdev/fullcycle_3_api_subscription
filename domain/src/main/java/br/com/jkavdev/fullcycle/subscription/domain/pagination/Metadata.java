package br.com.jkavdev.fullcycle.subscription.domain.pagination;

public record Metadata(
        int currentPage,
        int perPage,
        long total
) {

}
