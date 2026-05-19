package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

/**
 * Command to delete a Order
 */
public record DeleteOrderCommand(Long orderId) {

    /**
     * Constructor with validation
     */
    public DeleteOrderCommand {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("[DeleteOrderCommand] orderId must be a positive number");
        }
    }
}
