package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Order;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteOrderCommand;

import java.util.Optional;

/**
 * Service interface for handling Order-related commands.
 */
public interface OrderCommandService {

    /**
     * Handles the creation of a new Order.
     */
    Long handle(CreateOrderCommand command);

    /**
     * Handles the update of an existing Order.
     */
    Optional<Order> handle(UpdateOrderCommand command);

    /**
     * Handles the deletion of an existing Order.
     */
    void handle(DeleteOrderCommand command);
}
