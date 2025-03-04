package br.com.jkavdev.fullcycle.subscription.infrastructure.rest;

import br.com.jkavdev.fullcycle.subscription.ApiTest;
import br.com.jkavdev.fullcycle.subscription.ControllerTest;
import br.com.jkavdev.fullcycle.subscription.application.Presenter;
import br.com.jkavdev.fullcycle.subscription.application.plan.ChangePlan;
import br.com.jkavdev.fullcycle.subscription.application.plan.CreatePlan;
import br.com.jkavdev.fullcycle.subscription.domain.plan.PlanId;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.controllers.PlanRestController;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.CreatePlanResponse;
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

@ControllerTest(controllers = PlanRestController.class)
public class PlanRestApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreatePlan createPlan;

    @MockBean
    private ChangePlan changePlan;

    @Captor
    private ArgumentCaptor<CreatePlan.Input> createPlanInputCaptor;

    @Test
    public void givenValidInput_whenCreateSuccessfully_shouldReturnPlanId() throws Exception {
        // given
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedPrice = 10.00;
        final var expectedCurrency = "BRL";
        final var expectedActive = true;
        final var expectedPlanId = new PlanId(999L);

        Mockito.when(createPlan.execute(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenAnswer(call -> {
                    Presenter<CreatePlan.Output, CreatePlanResponse> p = call.getArgument(1);
                    return p.apply(new CreatePlanTestOutput(expectedPlanId));
                });

        final var json = """
                {
                "name": "%s",
                "description": "%s",
                "price": "%s",
                "currency": "%s",
                "active": %s
                }
                """.formatted(
                expectedName,
                expectedDescription,
                expectedPrice,
                expectedCurrency,
                expectedActive
        );

        // when
        final var aRequest = MockMvcRequestBuilders.post("/plans")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .with(ApiTest.admin());

        final var aResponse = mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/plans/" + expectedPlanId.value()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plan_id").value(Matchers.equalTo(expectedPlanId.value()), Long.TYPE));

        Mockito.verify(createPlan, Mockito.times(1))
                .execute(createPlanInputCaptor.capture(), ArgumentMatchers.any());

        final var actualRequest = createPlanInputCaptor.getValue();
        Assertions.assertEquals(expectedName, actualRequest.name());
        Assertions.assertEquals(expectedDescription, actualRequest.description());
        Assertions.assertEquals(expectedPrice, actualRequest.price());
        Assertions.assertEquals(expectedCurrency, actualRequest.currency());
        Assertions.assertEquals(expectedActive, actualRequest.active());

    }

    @Test
    public void givenEmptyName_shouldReturnError() throws Exception {
        // given
        final var expectedName = "  ";
        final var expectedDescription = "qualquerDescricao";
        final var expectedPrice = 10.00;
        final var expectedCurrency = "BRL";
        final var expectedActive = true;

        final var expectedErrorMessage = "must not be blank";
        final var expectedErrorProperty = "name";

        final var json = """
                {
                "name": "%s",
                "description": "%s",
                "price": "%s",
                "currency": "%s",
                "active": %s
                }
                """.formatted(
                expectedName,
                expectedDescription,
                expectedPrice,
                expectedCurrency,
                expectedActive
        );

        // when
        final var aRequest = MockMvcRequestBuilders.post("/plans")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .with(ApiTest.admin());

        final var aResponse = mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].property", Matchers.equalTo(expectedErrorProperty)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(createPlan, Mockito.never()).execute(ArgumentMatchers.any(), ArgumentMatchers.any());

    }

    record CreatePlanTestOutput(PlanId planId) implements CreatePlan.Output {

    }

}
