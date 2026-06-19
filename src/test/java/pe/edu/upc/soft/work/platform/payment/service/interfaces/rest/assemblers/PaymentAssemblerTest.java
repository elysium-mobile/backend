package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Payment;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreatePaymentRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.PaymentResponse;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.UpdatePaymentRequest;
import pe.edu.upc.soft.work.platform.payment.service.test.fixtures.PaymentCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreatePaymentRequest) -> maps orderId, transactionId and paymentDate (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreatePaymentRequest(
                PaymentCommandFixtures.VALID_ORDER_ID,
                PaymentCommandFixtures.VALID_TRANSACTION_ID,
                PaymentCommandFixtures.VALID_PAYMENT_DATE,
                PaymentCommandFixtures.VALID_PAYMENT_STATUS.toString(),
                PaymentCommandFixtures.VALID_METHOD
            );

        // Act
        CreatePaymentCommand command = PaymentAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.orderId()).isEqualTo(PaymentCommandFixtures.VALID_ORDER_ID);
        assertThat(command.transactionId()).isEqualTo(PaymentCommandFixtures.VALID_TRANSACTION_ID);
        assertThat(command.paymentDate()).isEqualTo(PaymentCommandFixtures.VALID_PAYMENT_DATE);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdatePaymentRequest) -> maps id and all fields to UpdatePaymentCommand (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdatePaymentRequest(
                PaymentCommandFixtures.VALID_ORDER_ID,
                PaymentCommandFixtures.VALID_TRANSACTION_ID,
                PaymentCommandFixtures.VALID_PAYMENT_DATE,
            PaymentCommandFixtures.VALID_PAYMENT_STATUS.toString(),
            PaymentCommandFixtures.VALID_METHOD);

        // Act
        UpdatePaymentCommand command = PaymentAssembler.toCommandFromRequest(51L, request);

        // Assert
        assertThat(command.paymentId()).isEqualTo(51L);
        assertThat(command.orderId()).isEqualTo(PaymentCommandFixtures.VALID_ORDER_ID);
        assertThat(command.transactionId()).isEqualTo(PaymentCommandFixtures.VALID_TRANSACTION_ID);
        assertThat(command.paymentDate()).isEqualTo(PaymentCommandFixtures.VALID_PAYMENT_DATE);
        assertThat(command.paymentMethod()).isEqualTo(PaymentCommandFixtures.VALID_METHOD);
    }

    @Test
    @DisplayName("toResponseFromEntity(Payment) -> maps every field to PaymentResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new Payment(PaymentCommandFixtures.validCreatePaymentCommand());
        ReflectionTestUtils.setId(entity, 51L);

        // Act
        PaymentResponse response = PaymentAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.paymentId()).isEqualTo(51L);
        assertThat(response.orderId()).isEqualTo(PaymentCommandFixtures.VALID_ORDER_ID);
        assertThat(response.transactionId()).isEqualTo(PaymentCommandFixtures.VALID_TRANSACTION_ID);
        assertThat(response.paymentDate()).isEqualTo(PaymentCommandFixtures.VALID_PAYMENT_DATE);
        assertThat(response.paymentMethod()).isEqualTo(PaymentCommandFixtures.VALID_METHOD);

    }
}
