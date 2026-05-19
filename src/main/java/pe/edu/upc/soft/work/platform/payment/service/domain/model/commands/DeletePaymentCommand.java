package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

/**
 * Command to delete a Payment
 */
public record DeletePaymentCommand(Long paymentId) {

    /**
     * Constructor with validation
     */
    public DeletePaymentCommand {
        if (paymentId == null || paymentId <= 0) {
            throw new IllegalArgumentException("[DeletePaymentCommand] paymentId must be a positive number");
        }
    }
}
