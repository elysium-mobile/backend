package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Order;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreateOrderRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.OrderResponse;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.UpdateOrderRequest;
import pe.edu.upc.soft.work.platform.payment.service.test.fixtures.PaymentCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class OrderAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateOrderRequest) -> wraps userAccountId in VO and maps fields (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateOrderRequest(
                PaymentCommandFixtures.VALID_USER_ACCOUNT_ID,
                PaymentCommandFixtures.VALID_ORDER_AMOUNT,
                PaymentCommandFixtures.VALID_MEMBERSHIP_ID);

        // Act
        CreateOrderCommand command = OrderAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.userAccountId().userAccountId()).isEqualTo(PaymentCommandFixtures.VALID_USER_ACCOUNT_ID);
        assertThat(command.amount()).isEqualTo(PaymentCommandFixtures.VALID_ORDER_AMOUNT);
        assertThat(command.membershipId()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_ID);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateOrderRequest) -> maps id, wraps userAccountId VO and fields (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateOrderRequest(
                PaymentCommandFixtures.VALID_USER_ACCOUNT_ID,
                PaymentCommandFixtures.VALID_ORDER_AMOUNT,
                PaymentCommandFixtures.VALID_MEMBERSHIP_ID);

        // Act
        UpdateOrderCommand command = OrderAssembler.toCommandFromRequest(41L, request);

        // Assert
        assertThat(command.orderId()).isEqualTo(41L);
        assertThat(command.userAccountId().userAccountId()).isEqualTo(PaymentCommandFixtures.VALID_USER_ACCOUNT_ID);
        assertThat(command.amount()).isEqualTo(PaymentCommandFixtures.VALID_ORDER_AMOUNT);
        assertThat(command.membershipId()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_ID);
    }

    @Test
    @DisplayName("toResponseFromEntity(Order) -> unwraps userAccountId VO and maps every field (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new Order(PaymentCommandFixtures.validCreateOrderCommand());
        ReflectionTestUtils.setId(entity, 41L);

        // Act
        OrderResponse response = OrderAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.orderId()).isEqualTo(41L);
        assertThat(response.userAccountId()).isEqualTo(PaymentCommandFixtures.VALID_USER_ACCOUNT_ID);
        assertThat(response.amount()).isEqualTo(PaymentCommandFixtures.VALID_ORDER_AMOUNT);
        assertThat(response.membershipId()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_ID);
    }
}
