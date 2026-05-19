package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.UserAccountId;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Order
 */
public record UpdateOrderCommand(Long orderId, UserAccountId userAccountId, Integer amount, Long membershipId) {

    /**
     * Constructor with validation
     */
    public UpdateOrderCommand {
        Objects.requireNonNull(orderId, "[UpdateOrderCommand] orderId must not be null");
    }
}
