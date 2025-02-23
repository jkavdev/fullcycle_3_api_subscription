package br.com.jkavdev.fullcycle.subscription.domain.person;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DocumentTest {

    @Test
    public void givenValidCpf_whenInstantiate_shouldReturnValueObject() {
        // given
        final var expectedDocumentValue = "12345678910";
        final var expectedDocumentType = "cpf";

        // when
        final var actualDocument = Document.create(expectedDocumentValue, expectedDocumentType);

        // then
        Assertions.assertEquals(expectedDocumentValue, actualDocument.value());
        Assertions.assertEquals(expectedDocumentType, actualDocument.type());
        Assertions.assertInstanceOf(Document.Cpf.class, actualDocument);
    }

    @Test
    public void givenValidCnpj_whenInstantiate_shouldReturnValueObject() {
        // given
        final var expectedDocumentValue = "12345678910111";
        final var expectedDocumentType = "cnpj";

        // when
        final var actualDocument = Document.create(expectedDocumentValue, expectedDocumentType);

        // then
        Assertions.assertEquals(expectedDocumentValue, actualDocument.value());
        Assertions.assertEquals(expectedDocumentType, actualDocument.type());
        Assertions.assertInstanceOf(Document.Cnpj.class, actualDocument);
    }

    @Test
    public void givenInvalidCpfLength_whenInstantiate_shouldReturnError() {
        // given
        final var expectedDocumentValue = "123";
        final var expectedDocumentType = "cpf";

        final var expectedErrorMessage = "'cpf' is invalid";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> Document.create(expectedDocumentValue, expectedDocumentType));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullCpf_whenInstantiate_shouldReturnError() {
        // given
        final String expectedDocumentValue = null;
        final var expectedDocumentType = "cpf";

        final var expectedErrorMessage = "'cpf' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> Document.create(expectedDocumentValue, expectedDocumentType));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenEmptyCpf_whenInstantiate_shouldReturnError() {
        // given
        final var expectedDocumentValue = "  ";
        final var expectedDocumentType = "cpf";

        final var expectedErrorMessage = "'cpf' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> Document.create(expectedDocumentValue, expectedDocumentType));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidCnpjLength_whenInstantiate_shouldReturnError() {
        // given
        final var expectedDocumentValue = "123";
        final var expectedDocumentType = "cnpj";

        final var expectedErrorMessage = "'cnpj' is invalid";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> Document.create(expectedDocumentValue, expectedDocumentType));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullCnpj_whenInstantiate_shouldReturnError() {
        // given
        final String expectedDocumentValue = null;
        final var expectedDocumentType = "cnpj";

        final var expectedErrorMessage = "'cnpj' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> Document.create(expectedDocumentValue, expectedDocumentType));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenEmptyCnpj_whenInstantiate_shouldReturnError() {
        // given
        final var expectedDocumentValue = "  ";
        final var expectedDocumentType = "cnpj";

        final var expectedErrorMessage = "'cnpj' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> Document.create(expectedDocumentValue, expectedDocumentType));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidDocument_whenInstantiate_shouldReturnError() {
        // given
        final var expectedDocumentValue = "  ";
        final var expectedDocumentType = "  ";

        final var expectedErrorMessage = "invalid document type";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> Document.create(expectedDocumentValue, expectedDocumentType));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}