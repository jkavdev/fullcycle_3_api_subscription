package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req;

import br.com.jkavdev.fullcycle.subscription.application.account.CreateAccount;
import br.com.jkavdev.fullcycle.subscription.application.account.CreateIdpUser;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank @Size(max = 255) String firstname,
        @NotBlank @Size(max = 255) String lastname,
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(max = 28) String password,
        @NotBlank String documentType,
        @NotBlank @Size(min = 11, max = 14) String documentNumber,
        String userId,
        String accountId
) implements CreateIdpUser.Input, CreateAccount.Input {

    @JsonCreator
    public SignUpRequest(
            @JsonProperty("document_number") String documentNumber,
            @JsonProperty("document_type") String documentType,
            @JsonProperty("password") String password,
            @JsonProperty("email") String email,
            @JsonProperty("lastname") String lastname,
            @JsonProperty("firstname") String firstname
    ) {
        this(
                documentNumber,
                documentType,
                password,
                email,
                lastname,
                firstname,
                "",
                ""
        );
    }

    public SignUpRequest with(final CreateIdpUser.Output output) {
        return new SignUpRequest(
                documentNumber(),
                documentType(),
                password(),
                email(),
                lastname(),
                firstname(),
                output.idpUserId().value(),
                accountId()
        );
    }

    public SignUpRequest with(final AccountId accountId) {
        return new SignUpRequest(
                documentNumber(),
                documentType(),
                password(),
                email(),
                lastname(),
                firstname(),
                userId(),
                accountId.value()
        );
    }

}
