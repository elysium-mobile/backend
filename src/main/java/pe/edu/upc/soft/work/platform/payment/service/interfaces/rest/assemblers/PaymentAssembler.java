package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Payment;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreatePaymentRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.UpdatePaymentRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.PaymentResponse;

public class PaymentAssembler {

    /**
     * Converts a CreatePaymentRequest to a CreatePaymentCommand.
     */
    public static CreatePaymentCommand toCommandFromRequest(CreatePaymentRequest request) {
        return new CreatePaymentCommand(request.orderId(), request.transactionId(), request.paymentDate());
    }

    /**
     * Converts an UpdatePaymentRequest to an UpdatePaymentCommand.
     */
    public static UpdatePaymentCommand toCommandFromRequest(Long paymentId, UpdatePaymentRequest request) {
        return new UpdatePaymentCommand(paymentId, request.orderId(), request.transactionId(), request.paymentDate());
    }

    /**
     * Converts a Payment entity to a PaymentResponse.
     */
    public static PaymentResponse toResponseFromEntity(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getOrderId(), payment.getTransactionId(), payment.getPaymentDate());
    }
}
