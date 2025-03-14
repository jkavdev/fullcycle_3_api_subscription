package br.com.jkavdev.fullcycle.subscription.infrastructure.mediator;

import br.com.jkavdev.fullcycle.subscription.application.Presenter;
import br.com.jkavdev.fullcycle.subscription.application.account.CreateAccount;
import br.com.jkavdev.fullcycle.subscription.application.account.CreateIdpUser;
import br.com.jkavdev.fullcycle.subscription.domain.UnitTest;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountGateway;
import br.com.jkavdev.fullcycle.subscription.domain.account.AccountId;
import br.com.jkavdev.fullcycle.subscription.domain.account.idp.UserId;
import br.com.jkavdev.fullcycle.subscription.domain.person.Document;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.req.SignUpRequest;
import br.com.jkavdev.fullcycle.subscription.infrastructure.rest.models.res.SignUpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class SignUpMediatorTest extends UnitTest {

    @InjectMocks
    private SignUpMediator signUpMediator;

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private CreateAccount createAccount;

    @Mock
    private CreateIdpUser createIdpUser;

    @Captor
    private ArgumentCaptor<CreateAccount.Input> createAccountInputCaptor;

    @Test
    public void givenValidRequest_whenSignUpSuccessfully_shouldReturnAccountId() throws Exception {
        // given
        final var expectedFirstname = "qualquerNome";
        final var expectedLastname = "qualquerSobrenome";
        final var expectedEmail = "qualquerEmail@email";
        final var expectedDocumentNumber = "12345678910";
        final var expectedDocumentType = Document.Cpf.TYPE;
        final var expectedAccountId = new AccountId("acc-123");
        final var expectedPassword = "12345678910";
        final var expectedUserId = new UserId("user-123");

        final var req = new SignUpRequest(
                expectedFirstname,
                expectedLastname,
                expectedEmail,
                expectedPassword,
                expectedDocumentType,
                expectedDocumentNumber
        );

        Mockito.when(accountGateway.nextId())
                .thenReturn(expectedAccountId);
        Mockito.when(createIdpUser.execute(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenAnswer(t -> {
                    final Presenter<CreateIdpUser.Output, SignUpRequest> a2 = t.getArgument(1);
                    return a2.apply(() -> expectedUserId);
                });
        Mockito.when(createAccount.execute(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenAnswer(t -> {
                    final Presenter<CreateAccount.Output, SignUpResponse> a2 = t.getArgument(1);
                    return a2.apply(() -> expectedAccountId);
                });

        // when
        final var actualOutput = signUpMediator.signUp(req);

        // then
        Assertions.assertEquals(expectedAccountId.value(), actualOutput.accountId());

        Mockito.verify(createAccount, Mockito.times(1))
                .execute(createAccountInputCaptor.capture(), ArgumentMatchers.any());

        final var actualInput = createAccountInputCaptor.getValue();
        Assertions.assertEquals(expectedUserId.value(), actualInput.userId());
        Assertions.assertEquals(expectedAccountId.value(), actualInput.accountId());

    }

}