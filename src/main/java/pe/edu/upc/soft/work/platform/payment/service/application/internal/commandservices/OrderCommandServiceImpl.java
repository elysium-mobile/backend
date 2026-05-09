package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Order;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.OrderCommandService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.OrderRepository;

import java.util.Optional;

@Service
public class OrderCommandServiceImpl implements OrderCommandService {
    private final OrderRepository orderRepository;

    public OrderCommandServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Long handle(CreateOrderCommand command) {
        var order = new Order(command);
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Order: " + e.getMessage(), e);
        }
        return order.getId();
    }

    @Override
    public Optional<Order> handle(UpdateOrderCommand command) {
        var orderId = command.orderId();
        if (!this.orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order with ID " + orderId + " does not exist.");
        }

        var orderToUpdate = this.orderRepository.findById(orderId).get();
        orderToUpdate.updateOrder(command);
        try {
            var updatedOrder = this.orderRepository.save(orderToUpdate);
            return Optional.of(updatedOrder);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Order: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(DeleteOrderCommand command) {
        if (!orderRepository.existsById(command.orderId())) {
            throw new RuntimeException("Order with ID " + command.orderId() + " does not exist.");
        }
        try {
            orderRepository.deleteById(command.orderId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Order: " + e.getMessage(), e);
        }
    }
}
