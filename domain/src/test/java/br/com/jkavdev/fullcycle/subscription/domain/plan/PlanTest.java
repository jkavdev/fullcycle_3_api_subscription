package br.com.jkavdev.fullcycle.subscription.domain.plan;

import br.com.jkavdev.fullcycle.subscription.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.subscription.domain.money.Money;
import br.com.jkavdev.fullcycle.subscription.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

/**
 * 1 - caminho feliz de um novo agregado
 * 2 - caminho feliz restauracao do agregado
 * 3 - caminho de validacao
 */
public class PlanTest {

    @Test
    public void givenValidParams_whenCallsNewPlan_shouldInstantiate() {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 0;
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedActive = true;
        final var expectedPrice = new Money("BRL", 20.99);

        // when
        final var actualPlan =
                Plan.newPlan(expectedId, expectedName, expectedDescription, expectedActive, expectedPrice);

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertNotNull(actualPlan.createdAt());
        Assertions.assertNotNull(actualPlan.updatedAt());
        Assertions.assertNull(actualPlan.deletedAt());
    }

    @Test
    public void givenNullActive_whenCallsNewPlan_shouldInstantiate() {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 0;
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final Boolean expectedActive = null;
        final var expectedPrice = new Money("BRL", 20.99);

        // when
        final var actualPlan =
                Plan.newPlan(expectedId, expectedName, expectedDescription, expectedActive, expectedPrice);

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertFalse(actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertNotNull(actualPlan.createdAt());
        Assertions.assertNotNull(actualPlan.updatedAt());
        Assertions.assertNotNull(actualPlan.deletedAt());
    }

    @Test
    public void givenValidParams_whenCallsWith_shouldInstantiate() {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 0;
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedActive = true;
        final var expectedPrice = new Money("BRL", 20.99);
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        // when
        final var actualPlan = Plan.with(
                expectedId,
                expectedVersion,
                expectedName,
                expectedDescription,
                expectedActive,
                expectedPrice,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt
        );

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertEquals(expectedCreatedAt, actualPlan.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualPlan.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, actualPlan.deletedAt());
    }

    @Test
    public void givenInvalidId_whenCallsWith_shouldReturnError() {
        // given
        final PlanId expectedId = null;
        final var expectedVersion = 0;
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedActive = true;
        final var expectedPrice = new Money("BRL", 20.99);
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        final var expectedErrorMessage = "'id' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Plan.with(
                        expectedId,
                        expectedVersion,
                        expectedName,
                        expectedDescription,
                        expectedActive,
                        expectedPrice,
                        expectedCreatedAt,
                        expectedUpdatedAt,
                        expectedDeletedAt
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidName_whenCallsWith_shouldReturnError() {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 0;
        final var expectedName = " ";
        final var expectedDescription = "qualquerDescricao";
        final var expectedActive = true;
        final var expectedPrice = new Money("BRL", 20.99);
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Plan.with(
                        expectedId,
                        expectedVersion,
                        expectedName,
                        expectedDescription,
                        expectedActive,
                        expectedPrice,
                        expectedCreatedAt,
                        expectedUpdatedAt,
                        expectedDeletedAt
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullName_whenCallsWith_shouldReturnError() {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 0;
        final String expectedName = null;
        final var expectedDescription = "qualquerDescricao";
        final var expectedActive = true;
        final var expectedPrice = new Money("BRL", 20.99);
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Plan.with(
                        expectedId,
                        expectedVersion,
                        expectedName,
                        expectedDescription,
                        expectedActive,
                        expectedPrice,
                        expectedCreatedAt,
                        expectedUpdatedAt,
                        expectedDeletedAt
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenInvalidDescription_whenCallsWith_shouldReturnError() {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 0;
        final var expectedName = "qualquerNome";
        final var expectedDescription = " ";
        final var expectedActive = true;
        final var expectedPrice = new Money("BRL", 20.99);
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        final var expectedErrorMessage = "'description' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Plan.with(
                        expectedId,
                        expectedVersion,
                        expectedName,
                        expectedDescription,
                        expectedActive,
                        expectedPrice,
                        expectedCreatedAt,
                        expectedUpdatedAt,
                        expectedDeletedAt
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullDescription_whenCallsWith_shouldReturnError() {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 0;
        final var expectedName = "qualquerNome";
        final String expectedDescription = null;
        final var expectedActive = true;
        final var expectedPrice = new Money("BRL", 20.99);
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        final var expectedErrorMessage = "'description' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Plan.with(
                        expectedId,
                        expectedVersion,
                        expectedName,
                        expectedDescription,
                        expectedActive,
                        expectedPrice,
                        expectedCreatedAt,
                        expectedUpdatedAt,
                        expectedDeletedAt
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullActive_whenCallsWith_shouldReturnError() {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 0;
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final Boolean expectedActive = null;
        final var expectedPrice = new Money("BRL", 20.99);
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        final var expectedErrorMessage = "'active' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Plan.with(
                        expectedId,
                        expectedVersion,
                        expectedName,
                        expectedDescription,
                        expectedActive,
                        expectedPrice,
                        expectedCreatedAt,
                        expectedUpdatedAt,
                        expectedDeletedAt
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void given0AsPrice_whenCallsWith_shouldReturnOK() {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 0;
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedActive = true;
        final var expectedPrice = new Money("BRL", 0.0);
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        // when
        final var actualPlan = Plan.with(
                expectedId,
                expectedVersion,
                expectedName,
                expectedDescription,
                expectedActive,
                expectedPrice,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt
        );

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertEquals(expectedCreatedAt, actualPlan.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualPlan.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, actualPlan.deletedAt());
    }

    @Test
    public void givenNullCreatedAt_whenCallsWith_shouldReturnError() {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 0;
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedActive = true;
        final var expectedPrice = new Money("BRL", 20.99);
        final Instant expectedCreatedAt = null;
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        final var expectedErrorMessage = "'createdAt' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Plan.with(
                        expectedId,
                        expectedVersion,
                        expectedName,
                        expectedDescription,
                        expectedActive,
                        expectedPrice,
                        expectedCreatedAt,
                        expectedUpdatedAt,
                        expectedDeletedAt
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullUpdatedAt_whenCallsWith_shouldReturnError() {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 0;
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedActive = true;
        final var expectedPrice = new Money("BRL", 20.99);
        final var expectedCreatedAt = InstantUtils.now();
        final Instant expectedUpdatedAt = null;
        final var expectedDeletedAt = InstantUtils.now();

        final var expectedErrorMessage = "'updatedAt' should not be null";

        // when
        final var actualException = Assertions.assertThrows(DomainException.class, () ->
                Plan.with(
                        expectedId,
                        expectedVersion,
                        expectedName,
                        expectedDescription,
                        expectedActive,
                        expectedPrice,
                        expectedCreatedAt,
                        expectedUpdatedAt,
                        expectedDeletedAt
                ));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenNullDeletedAt_whenCallsWith_shouldReturnOK() {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 0;
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedActive = true;
        final var expectedPrice = new Money("BRL", 20.99);
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final Instant expectedDeletedAt = null;

        // when
        final var actualPlan = Plan.with(
                expectedId,
                expectedVersion,
                expectedName,
                expectedDescription,
                expectedActive,
                expectedPrice,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt
        );

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertEquals(expectedCreatedAt, actualPlan.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualPlan.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, actualPlan.deletedAt());
    }

    @Test
    public void givenPlan_whenExecuteWithoutCommands_shouldDoNothing() {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 1;
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedActive = true;
        final var expectedPrice = new Money("BRL", 20.99);

        final var actualPlan =
                Plan.newPlan(expectedId, expectedName, expectedDescription, expectedActive, expectedPrice);

        // when
        actualPlan.execute();

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertNotNull(actualPlan.createdAt());
        Assertions.assertNotNull(actualPlan.updatedAt());
        Assertions.assertNull(actualPlan.deletedAt());
    }

    @Test
    public void givenActivePlan_whenExecuteInactiveCommand_shouldInactivate() throws InterruptedException {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 1;
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedActive = true;
        final var expectedPrice = new Money("BRL", 20.99);

        final var actualPlan =
                Plan.newPlan(expectedId, expectedName, expectedDescription, expectedActive, expectedPrice);
        Assertions.assertTrue(actualPlan.active());
        Assertions.assertNull(actualPlan.deletedAt());

        Thread.sleep(1); // para que o updatedat seja atualizado com um valor diferente

        // when
        actualPlan.execute(new PlanCommand.InactivePlan());

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertFalse(actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertNotNull(actualPlan.createdAt());
        Assertions.assertTrue(actualPlan.createdAt().isBefore(actualPlan.updatedAt()));
        Assertions.assertNotNull(actualPlan.deletedAt());
    }

    @Test
    public void givenInactivePlan_whenExecuteActiveCommand_shouldActivate() throws InterruptedException {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 1;
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedActive = false;
        final var expectedPrice = new Money("BRL", 20.99);

        final var actualPlan =
                Plan.newPlan(expectedId, expectedName, expectedDescription, expectedActive, expectedPrice);
        Assertions.assertFalse(actualPlan.active());
        Assertions.assertNotNull(actualPlan.deletedAt());

        Thread.sleep(1); // para que o updatedat seja atualizado com um valor diferente

        // when
        actualPlan.execute(new PlanCommand.ActivatePlan());

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertTrue(actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertNotNull(actualPlan.createdAt());
        Assertions.assertTrue(actualPlan.createdAt().isBefore(actualPlan.updatedAt()));
        Assertions.assertNull(actualPlan.deletedAt());
    }

    @Test
    public void givenPlanWithActiveFalse_whenExecuteChangeCommand_shouldUpdateAttributesAndInactivate() throws InterruptedException {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 1;
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedActive = true;
        final var expectedPrice = new Money("BRL", 20.99);

        final var actualPlan =
                Plan.newPlan(expectedId, expectedName, expectedDescription, expectedActive, expectedPrice);

        final var expectedNewName = "qualquerNovoNome";
        final var expectedNewDescription = "qualquerNovoDescricao";
        final var expectedNewActive = false;

        Thread.sleep(1); // para que o updatedat seja atualizado com um valor diferente

        // when
        actualPlan.execute(new PlanCommand.ChangePlan(expectedNewName, expectedNewDescription, expectedNewActive));

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedNewName, actualPlan.name());
        Assertions.assertEquals(expectedNewDescription, actualPlan.description());
        Assertions.assertFalse(actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertNotNull(actualPlan.createdAt());
        Assertions.assertTrue(actualPlan.createdAt().isBefore(actualPlan.updatedAt()));
        Assertions.assertNotNull(actualPlan.deletedAt());
    }

    @Test
    public void givenPlanWithActiveTrue_whenExecuteChangeCommand_shouldUpdateAttributesAndActivate() throws InterruptedException {
        // given
        final var expectedId = new PlanId(123L);
        final var expectedVersion = 1;
        final var expectedName = "qualquerNome";
        final var expectedDescription = "qualquerDescricao";
        final var expectedActive = false;
        final var expectedPrice = new Money("BRL", 20.99);

        final var actualPlan =
                Plan.newPlan(expectedId, expectedName, expectedDescription, expectedActive, expectedPrice);

        final var expectedNewName = "qualquerNovoNome";
        final var expectedNewDescription = "qualquerNovoDescricao";
        final var expectedNewActive = true;

        Thread.sleep(1); // para que o updatedat seja atualizado com um valor diferente

        // when
        actualPlan.execute(new PlanCommand.ChangePlan(expectedNewName, expectedNewDescription, expectedNewActive));

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedNewName, actualPlan.name());
        Assertions.assertEquals(expectedNewDescription, actualPlan.description());
        Assertions.assertTrue(actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertNotNull(actualPlan.createdAt());
        Assertions.assertTrue(actualPlan.createdAt().isBefore(actualPlan.updatedAt()));
        Assertions.assertNull(actualPlan.deletedAt());
    }

}
