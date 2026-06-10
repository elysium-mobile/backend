package pe.edu.upc.soft.work.platform.payment.service.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.MembershipActivatedEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.PaymentRegisteredEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.StripePaymentSucceededEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.MembershipStatus;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.OrderRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.PaymentRepository;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Payment;

import java.util.Date;

/**
 * StripePaymentSucceededEventHandler
 * Listens for StripePaymentSucceededEvent and:
 *   1. Creates a Payment record using the Stripe PaymentIntent ID as transactionId.
 *   2. Activates the Membership associated with the Order.
 *   3. Publishes PaymentRegisteredEvent and MembershipActivatedEvent.
 */
@Service
public class StripePaymentSucceededEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StripePaymentSucceededEventHandler.class);

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final MembershipRepository membershipRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Constructor for StripePaymentSucceededEventHandler.
     * @param paymentRepository    repository for Payment persistence
     * @param orderRepository      repository to look up the Order
     * @param membershipRepository repository to look up and update the Membership
     * @param eventPublisher       Spring event publisher for downstream events
     */
    public StripePaymentSucceededEventHandler(PaymentRepository paymentRepository,
                                              OrderRepository orderRepository,
                                              MembershipRepository membershipRepository,
                                              ApplicationEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.membershipRepository = membershipRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Handles the StripePaymentSucceededEvent.
     * Creates a Payment record, activates the related Membership,
     * and publishes the corresponding domain events.
     *
     * @param event the succeeded payment event from the Stripe webhook handler
     */
    @EventListener
    public void on(StripePaymentSucceededEvent event) {
        LOGGER.info("[StripePaymentSucceededEventHandler] Processing succeeded payment for Order ID: {}", event.getOrderId());

        var order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new NotFoundArgumentException(
                        String.format("[StripePaymentSucceededEventHandler] Order ID: %s not found",
                                event.getOrderId())));

        var createPaymentCommand = new CreatePaymentCommand(
                order.getId(),
                event.getStripePaymentIntentId(),
                new Date());
        var payment = new Payment(createPaymentCommand);
        paymentRepository.save(payment);
        eventPublisher.publishEvent(new PaymentRegisteredEvent(this, payment.getId(), payment.getOrderId()));
        LOGGER.info("[StripePaymentSucceededEventHandler] Payment created with ID: {}", payment.getId());

        var membership = membershipRepository.findById(order.getMembershipId())
                .orElseThrow(() -> new NotFoundArgumentException(
                        String.format("[StripePaymentSucceededEventHandler] Membership ID: %s not found",
                                order.getMembershipId())));

        membership.updateMembership(new UpdateMembershipCommand(
                membership.getId(),
                membership.getMembershipStart(),
                membership.getMembershipOver(),
                MembershipStatus.ACTIVE));
        membershipRepository.save(membership);
        eventPublisher.publishEvent(new MembershipActivatedEvent(this, membership.getId(), MembershipStatus.ACTIVE));
        LOGGER.info("[StripePaymentSucceededEventHandler] Membership ID: {} set to ACTIVE", membership.getId());
    }
}
