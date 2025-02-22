package br.com.jkavdev.fullcycle.subscription.domain.person;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;

public final class DocumentFactory {

    private DocumentFactory() {

    }

    public static Document create(final String documentNumber, final String documentType) {
        return switch (documentType) {
            case Document.Cpf.TYPE -> new Document.Cpf(documentNumber);
            case Document.Cnpj.TYPE -> new Document.Cnpj(documentNumber);
            default -> throw DomainException.with("invalid document type");
        };
    }

}
