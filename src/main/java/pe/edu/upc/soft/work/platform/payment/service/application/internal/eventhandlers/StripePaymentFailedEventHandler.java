package pe.edu.upc.soft.work.platform.payment.service.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.StripePaymentFailedEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.MembershipStatus;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.OrderRepository;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

/**
 * StripePaymentFailedEventHandler
 * Listens for StripePaymentFailedEvent and marks the Membership
 * associated with the failed Order as FAILED so the user knows
 * the subscription was not activated.
 */
@Service
public class StripePaymentFailedEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StripePaymentFailedEventHandler.class);

    private final OrderRepository orderRepository;
    private final MembershipRepository membershipRepository;

    /**
     * Constructor for StripePaymentFailedEventHandler.
     * @param orderRepository      repository to look up the Order
     * @param membershipRepository repository to look up and update the Membership
     */
    public StripePaymentFailedEventHandler(OrderRepository orderRepository,
                                           MembershipRepository membershipRepository) {
        this.orderRepository = orderRepository;
        this.membershipRepository = membershipRepository;
    }

    /**
     * Handles the StripePaymentFailedEvent.
     * Sets the related Membership status to FAILED and logs the reason.
     *
     * @param event the failed payment event from the Stripe webhook handler
     */
    @EventListener
    public void on(StripePaymentFailedEvent event) {
        LOGGER.warn("[StripePaymentFailedEventHandler] Payment failed for Order ID: {}. Reason: {}",
                event.getOrderId(), event.getFailureReason());

        var order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new NotFoundArgumentException(
                        String.format("[StripePaymentFailedEventHandler] Order ID: %s not found",
                                event.getOrderId())));

        var membership = membershipRepository.findById(order.getMembershipId())
                .orElseThrow(() -> new NotFoundArgumentException(
                        String.format("[StripePaymentFailedEventHandler] Membership ID: %s not found",
                                order.getMembershipId())));

        membership.updateMembership(new UpdateMembershipCommand(
                membership.getId(),
                membership.getMembershipStart(),
                membership.getMembershipOver(),
                MembershipStatus.FAILED));
        membershipRepository.save(membership);
        LOGGER.warn("[StripePaymentFailedEventHandler] Membership ID: {} set to FAILED", membership.getId());
    }
}
