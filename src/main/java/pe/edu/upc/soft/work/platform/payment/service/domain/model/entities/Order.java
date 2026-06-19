package pe.edu.upc.soft.work.platform.payment.service.domain.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateOrderCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.UserAccountId;

/**
 * Order aggregate root entity.
 */
@Entity
@Table(name="orders")
public class Order extends AuditableAbstractAggregateRoot<Order> {

    @Getter
    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "user_account_id", nullable = false, length = 50))
    )
    @JsonProperty("id_user_account")
    private UserAccountId userAccountId;
    @Getter
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Setter
    @Getter
    @Column(name = "membership_id", nullable = false)
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
