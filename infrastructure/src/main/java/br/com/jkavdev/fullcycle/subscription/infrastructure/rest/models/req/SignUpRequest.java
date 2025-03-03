package br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req;

import br.com.jkavdev.fullcycle.subscription.application.account.CreateIdpUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank @Size(max = 255) String firstname,
        @NotBlank @Size(max = 255) String lastname,
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(max = 28) String password,
        @NotBlank String documentType,
        @NotBlank @Size(min = 11, max = 14) String documentNumber
) implements CreateIdpUser.Input {
}
