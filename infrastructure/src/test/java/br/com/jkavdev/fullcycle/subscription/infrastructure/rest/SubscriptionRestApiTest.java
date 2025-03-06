package br.com.jkavdev.fullcycle.subscription.infrastructure.rest;

import br.com.jkavdev.fullcycle.subscription.ApiTest;
import br.com.jkavdev.fullcycle.subscription.ControllerTest;
import br.com.jkavdev.fullcycle.subscription.application.Presenter;
import br.com.jkavdev.fullcycle.subscription.application.subscription.CancelSubscription;
import br.com.jkavdev.fullcycle.subscription.application.subscription.CreateSubscription;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.SubscriptionId;
import br.com.jkavdev.fullcycle.subscription.domain.subscription.status.SubscriptionStatus;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.controllers.SubscriptionRestController;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.CancelSubscriptionResponse;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.CreateSubscriptionResponse;
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

/**
 * 1 - tornar o id do idp (keycloak) o mesmo id do usuario interno da aplicacao (account id)
 * 2 - descobrir o account id com base no subject do jwt
 * 3 - colocar o account id dentro dos claims do jwt
 */
@ControllerTest(controllers = SubscriptionRestController.class)
public class SubscriptionRestApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateSubscription createSubscription;

    @MockBean
    private CancelSubscription cancelSubscription;

    @Captor
    private ArgumentCaptor<CreateSubscription.Input> createSubscriptionInputCaptor;

    @Captor
    private ArgumentCaptor<CancelSubscription.Input> cancelSubscriptionInputCaptor;

    @Test
    public void givenValidInput_whenCreateSuccessfully_shouldReturnSubscriptionId() throws Exception {
        // given
        final var expectedPlanId = 132L;
        final var expectedAccountId = "132";
        final var expectedSubscriptionId = new SubscriptionId("sub-123");

        Mockito.when(createSubscription.execute(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenAnswer(call -> {
                    Presenter<CreateSubscription.Output, CreateSubscriptionResponse> p = call.getArgument(1);
                    return p.apply(new CreateSubscriptionTestOutput(expectedSubscriptionId));
                });

        final var json = """
                {
                "plan_id": "%s"
                }
                """.formatted(
                expectedPlanId
        );

        // when
        final var aRequest = MockMvcRequestBuilders.post("/subscriptions")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .with(ApiTest.admin(expectedAccountId));

        final var aResponse = mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/subscriptions/" + expectedSubscriptionId.value()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subscription_id").value(Matchers.equalTo(expectedSubscriptionId.value())));

        Mockito.verify(createSubscription, Mockito.times(1))
                .execute(createSubscriptionInputCaptor.capture(), ArgumentMatchers.any());

        final var actualRequest = createSubscriptionInputCaptor.getValue();
        Assertions.assertEquals(expectedPlanId, actualRequest.planId());
        Assertions.assertEquals(expectedAccountId, actualRequest.accountId());

    }

    @Test
    public void givenValidAccountId_whenCanceledSuccessfully_shouldReturnNewSubscriptionStatus() throws Exception {
        // given
        final var expectedPlanId = 132L;
        final var expectedAccountId = "132";
        final var expectedSubscriptionId = new SubscriptionId("sub-123");
        final var expectedSubscriptionStatus = SubscriptionStatus.CANCELED;

        Mockito.when(cancelSubscription.execute(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenAnswer(call -> {
                    Presenter<CancelSubscription.Output, CancelSubscriptionResponse> p = call.getArgument(1);
                    return p.apply(new CanceledSubscriptionTestOutput(expectedSubscriptionStatus, expectedSubscriptionId));
                });

        // when
        final var aRequest = MockMvcRequestBuilders.put("/subscriptions/active/cancel")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(ApiTest.admin(expectedAccountId));

        final var aResponse = mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subscription_status").value(Matchers.equalTo(expectedSubscriptionStatus)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subscription_id").value(Matchers.equalTo(expectedSubscriptionId.value())));

        Mockito.verify(cancelSubscription, Mockito.times(1))
                .execute(cancelSubscriptionInputCaptor.capture(), ArgumentMatchers.any());

        final var actualRequest = cancelSubscriptionInputCaptor.getValue();
        Assertions.assertEquals(expectedAccountId, actualRequest.accountId());

    }

    record CreateSubscriptionTestOutput(SubscriptionId subscriptionId) implements CreateSubscription.Output {

    }

    record CanceledSubscriptionTestOutput(
            String subscriptionStatus,
            SubscriptionId subscriptionId
    ) implements CancelSubscription.Output {

    }

}
