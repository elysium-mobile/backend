package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Payment;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeletePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Order;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.MembershipActivatedEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.PaymentRegisteredEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentCommandService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.OrderRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.PaymentRepository;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

import java.util.Optional;

/**
 * Service implementation for handling Payment commands
 */
@Service
public class PaymentCommandServiceImpl implements PaymentCommandService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final MembershipRepository membershipRepository;

    /**
     * Constructor for PaymentCommandServiceImpl
     * @param paymentRepository the repository for Payment persistence
     */
    public PaymentCommandServiceImpl(PaymentRepository paymentRepository,
                                     OrderRepository orderRepository,
                                     ApplicationEventPublisher eventPublisher,
                                     MembershipRepository membershipRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.eventPublisher=eventPublisher;
        this.membershipRepository = membershipRepository;
    }

    /**
     * Handles the creation of a Payment
     * @param command the command to create a Payment
     * @return the generated ID of the new Payment
     */
    @Override
    public Long handle(CreatePaymentCommand command) {
        if (!this.orderRepository.existsById(command.orderId()))
        {
            throw new NotFoundArgumentException(
                    String.format("[PaymentCommandServiceImpl] Order ID: %s not found in the Payment context",
                            command.orderId())
            );
        }
        var order= orderRepository.findById(command.orderId()).get();
        var payment = new Payment(command);
        eventPublisher.publishEvent(new PaymentRegisteredEvent(this, payment.getId(), payment.getOrderId()));
        try {
            paymentRepository.save(payment);
            var membership = membershipRepository.findById(order.getMembershipId())
                .orElseThrow(() -> new NotFoundArgumentException(
                    String.format("[PaymentCommandServiceImpl] Membership ID: %s not found in the Payment context",
                            order.getMembershipId())
                ));
            membership.updateMembership(new UpdateMembershipCommand(
                    membership.getId(),
                membership.getMembershipPlanId(),
                membership.getMembershipStart(),
                membership.getMembershipOver(),
                membership.getMembershipStatus()
                ));
            membershipRepository.save(membership);
            eventPublisher.publishEvent(new MembershipActivatedEvent(this, membership.getId(), membership.getMembershipStatus()));
        } catch (Exception e) {
            throw new RuntimeException("Error creating Payment: " + e.getMessage(), e);
        }
        return payment.getId();
    }

    /**
     * Handles the update of an existing Payment
     * @param command the command to update a Payment
     * @return the updated Payment as an Optional
     */
    @Override
    public Optional<Payment> handle(UpdatePaymentCommand command) {
        var paymentId = command.paymentId();
        if (!this.paymentRepository.existsById(paymentId)) {
            throw new RuntimeException("Payment with ID " + paymentId + " does not exist.");
        }

        var paymentToUpdate = this.paymentRepository.findById(paymentId).get();
        paymentToUpdate.updatePayment(command);
        try {
            var updatedPayment = this.paymentRepository.save(paymentToUpdate);
            return Optional.of(updatedPayment);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Payment: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of a Payment
     * @param command the command to delete a Payment
     */
    @Override
    public void handle(DeletePaymentCommand command) {
        if (!paymentRepository.existsById(command.paymentId())) {
            throw new RuntimeException("Payment with ID " + command.paymentId() + " does not exist.");
        }
        try {
            paymentRepository.deleteById(command.paymentId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Payment: " + e.getMessage(), e);
        }
    }
}
