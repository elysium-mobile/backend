package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.UserAccountId;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Order
 */
public record CreateOrderCommand(UserAccountId userAccountId, Integer amount, Long membershipId) {

    /**
     * Constructor with validation
     */
    public CreateOrderCommand {
        Objects.requireNonNull(userAccountId, "[CreateOrderCommand] userAccountId must not be null");
        Objects.requireNonNull(amount, "[CreateOrderCommand] amount must not be null");
        Objects.requireNonNull(membershipId, "[CreateOrderCommand] membershipId must not be null");
    }
}
