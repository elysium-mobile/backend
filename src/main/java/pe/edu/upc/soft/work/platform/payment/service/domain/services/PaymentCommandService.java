package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Payment;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeletePaymentCommand;

import java.util.Optional;

/**
 * Service interface for handling Payment-related commands.
 */
public interface PaymentCommandService {

    /**
     * Handles the creation of a new Payment.
     */
    Long handle(CreatePaymentCommand command);

    /**
     * Handles the update of an existing Payment.
     */
    Optional<Payment> handle(UpdatePaymentCommand command);

    /**
     * Handles the deletion of an existing Payment.
     */
    void handle(DeletePaymentCommand command);
}
