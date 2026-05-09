package pe.edu.upc.soft.work.platform.payment.service.domain.model.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateOrderCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.UserAccountId;

/**
 * Order aggregate root entity.
 */
@Entity
public class Order extends AuditableAbstractAggregateRoot<Order> {

    @Getter
    private UserAccountId userAccountId;
    @Getter
    private Integer amount;
    @Getter
    private Long membershipId;

    /**
     * Default constructor for JPA.
     */
    public Order() {}

    /**
     * Constructor to create a Order from a CreateOrderCommand.
     * @param command the command containing order details
     */
    public Order(CreateOrderCommand command) {
        this.userAccountId = command.userAccountId();
        this.amount = command.amount();
        this.membershipId = command.membershipId();
    }

    /**
     * Updates the Order with details from an UpdateOrderCommand.
     * @param command the command containing updated order details
     */
    public void updateOrder(UpdateOrderCommand command) {
        this.userAccountId = command.userAccountId();
        this.amount = command.amount();
        this.membershipId = command.membershipId();
    }
}
