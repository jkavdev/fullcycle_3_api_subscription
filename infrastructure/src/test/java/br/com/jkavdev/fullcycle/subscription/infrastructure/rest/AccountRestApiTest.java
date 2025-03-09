package br.com.jkavdev.fullcycle.subscription.infrastructure.rest;

import br.com.jkavdev.fullcycle.subscription.ControllerTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.person.Document;
import br.com.jkavdev.fullcycle.subscription.infrastructure.mediator.SignUpMediator;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.controllers.AccountRestController;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req.SignUpRequest;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.SignUpResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ControllerTest(controllers = AccountRestController.class)
public class AccountRestApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SignUpMediator signUpMediator;

    @Captor
    private ArgumentCaptor<SignUpRequest> signUpRequestCaptor;

    @Test
    public void givenValidInput_whenSignUpSuccessfully_shouldReturnAccountId() throws Exception {
        // given
        final var expectedFirstname = "qualquerNome";
        final var expectedLastname = "qualquerSobrenome";
        final var expectedEmail = "qualquerEmail@email";
        final var expectedDocumentNumber = "12345678910";
        final var expectedDocumentType = Document.Cpf.TYPE;
        final var expectedAccountId = new AccountId("qualquerId");
        final var expectedPassword = "12345678910";

        Mockito.when(signUpMediator.signUp(ArgumentMatchers.any()))
                .thenReturn(new SignUpResponse(expectedAccountId.value()));

        final var json = """
                {
                "firstname": "%s",
                "lastname": "%s",
                "email": "%s",
                "document_type": "%s",
                "document_number": "%s",
                "password": "%s"
                }
                """.formatted(
                expectedFirstname,
                expectedLastname,
                expectedEmail,
                expectedDocumentType,
                expectedDocumentNumber,
                expectedPassword
        );

        // when
        final var aRequest = MockMvcRequestBuilders.post("/accounts/sign-up")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json);

        final var aResponse = mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/accounts/" + expectedAccountId.value()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account_id").value(Matchers.equalTo(expectedAccountId.value())));

        Mockito.verify(signUpMediator, Mockito.times(1)).signUp(signUpRequestCaptor.capture());

        final var actualRequest = signUpRequestCaptor.getValue();
        Assertions.assertEquals(expectedFirstname, actualRequest.firstname());
        Assertions.assertEquals(expectedLastname, actualRequest.lastname());
        Assertions.assertEquals(expectedEmail, actualRequest.email());
        Assertions.assertEquals(expectedDocumentType, actualRequest.documentType());
        Assertions.assertEquals(expectedDocumentNumber, actualRequest.documentNumber());
        Assertions.assertEquals(expectedPassword, actualRequest.password());

    }

    @Test
    public void givenEmptyFirstname_shouldReturnError() throws Exception {
        // given
        final var expectedFirstname = "  ";
        final var expectedLastname = "qualquerSobrenome";
        final var expectedEmail = "qualquerEmail@email";
        final var expectedDocumentNumber = "12345678910";
        final var expectedDocumentType = Document.Cpf.TYPE;
        final var expectedPassword = "12345678910";

        final var expectedErrorMessage = "must not be blank";
        final var expectedErrorProperty = "firstname";

        final var json = """
                {
                "firstname": "%s",
                "lastname": "%s",
                "email": "%s",
                "document_type": "%s",
                "document_number": "%s",
                "password": "%s"
                }
                """.formatted(
                expectedFirstname,
                expectedLastname,
                expectedEmail,
                expectedDocumentType,
                expectedDocumentNumber,
                expectedPassword
        );

        // when
        final var aRequest = MockMvcRequestBuilders.post("/accounts/sign-up")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json);

        final var aResponse = mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].property", Matchers.equalTo(expectedErrorProperty)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(signUpMediator, Mockito.never()).signUp(ArgumentMatchers.any());

    }

}
