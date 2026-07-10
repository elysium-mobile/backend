package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.application.internal.outboundservices.acl.ExternalIamServiceFromPaymentService;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Order;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.MembershipStatus;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.OrderCommandService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.OrderRepository;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

import java.util.Date;
import java.util.Optional;

/**
 * Service implementation for handling Order commands
 */
@Service
@Transactional
public class OrderCommandServiceImpl implements OrderCommandService {
    private final OrderRepository orderRepository;
    private final ExternalIamServiceFromPaymentService externalIamServiceFromPaymentService;
    private final MembershipRepository membershipRepository;

    /**
     * Constructor for OrderCommandServiceImpl
     * @param orderRepository the repository for Order persistence
     */
    public OrderCommandServiceImpl(OrderRepository orderRepository,
                                   ExternalIamServiceFromPaymentService externalIamServiceFromPaymentService,
                                   MembershipRepository membershipRepository) {
        this.orderRepository = orderRepository;
        this.externalIamServiceFromPaymentService = externalIamServiceFromPaymentService;
        this.membershipRepository = membershipRepository;
    }

    /**
     * Handles the creation of an Order
     * @param command the command to create an Order
     * @return the generated ID of the new Order
     */
    @Override
    public Long handle(CreateOrderCommand command) {
        if (!externalIamServiceFromPaymentService.existsUserAccountById(command.userAccountId().userAccountId())){
            throw new NotFoundArgumentException(
                    String.format("[OrderCommandServiceImpl] User Account ID: %s not found in the external IAM service",
                            command.userAccountId().userAccountId()));
        }

        if (!membershipRepository.existsById(command.membershipId())){
            throw new NotFoundArgumentException(
                    String.format("[OrderCommandServiceImpl] Membership ID: %s not found in the external IAM service",
                            command.membershipId())
            );
        }
        var membership = membershipRepository.findById(command.membershipId()).get();
        if (membership.getMembershipStatus() != MembershipStatus.ACTIVE) {
            throw new IllegalStateException(
                String.format("[OrderCommandServiceImpl] Membership ID: %s is not active (current status: %s)",
                    command.membershipId(), membership.getMembershipStatus()));
        }
        var now = new Date();
        if (now.before(membership.getMembershipStart()) || now.after(membership.getMembershipOver())) {
            throw new IllegalStateException(
                String.format("[OrderCommandServiceImpl] Membership ID: %s is outside its validity period",
                    command.membershipId()));
        }

        var order = new Order(command);
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Order: " + e.getMessage(), e);
        }
        return order.getId();
    }

    /**
     * Handles the update of an existing Order
     * @param command the command to update an Order
     * @return the updated Order as an Optional
     */
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

    /**
     * Handles the deletion of an Order
     * @param command the command to delete an Order
     */
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
