package pe.edu.upc.soft.work.platform.payment.service.application.internal.retries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateStripeCheckoutSessionCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.RetryPaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.PaymentRetryInitiatedEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.PaymentStatus;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentGatewayAdapter;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.PaymentRepository;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

/**
 * PaymentRetryService
 * Handles logic for retrying failed payments by creating a new Stripe Checkout Session
 * and updating the Payment record with the new attempt.
 */
@Service
public class PaymentRetryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentRetryService.class);

    private final PaymentGatewayAdapter paymentGatewayAdapter;
    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentRetryService(PaymentGatewayAdapter paymentGatewayAdapter,
                               PaymentRepository paymentRepository,
                               ApplicationEventPublisher eventPublisher) {
        this.paymentGatewayAdapter = paymentGatewayAdapter;
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Retries a failed payment by creating a new Stripe Checkout Session.
     * The customer is redirected to the returned checkout URL to attempt
     * payment again. The original Payment record is updated with the new
     * PaymentIntent ID from the session.
     *
     * @param command the retry payment command
     * @return PaymentRetryResponse with the new checkout URL and transaction ID
     */
    @Transactional
    public PaymentRetryResponse retryPayment(RetryPaymentCommand command) {
        LOGGER.info("[PaymentRetryService] Retrying payment ID: {} for Order: {}",
            command.paymentId(), command.orderId());

        // Validate payment exists and is in FAILED state
        var payment = paymentRepository.findById(command.paymentId())
            .orElseThrow(() -> new NotFoundArgumentException(
                String.format("[PaymentRetryService] Payment ID: %s not found",
                    command.paymentId())));

        if (!payment.isFailed()) {
            throw new IllegalStateException(
                String.format("[PaymentRetryService] Cannot retry Payment in status: %s. Only FAILED payments can be retried.",
                    payment.getPaymentStatus()));
        }

        // Create a new Checkout Session through the gateway adapter.
        // IMPORTANT: this must use a DIFFERENT idempotency key than the original
        // checkout (retry-payment-{paymentId}-{uuid}, not checkout-session-{orderId}).
        // Reusing the checkout key here would make Stripe return the same, already
        // failed session instead of creating a fresh one to retry with.
        var idempotencyKey = "retry-payment-" + command.paymentId() + "-" + java.util.UUID.randomUUID();
        var gatewayCommand = new CreateStripeCheckoutSessionCommand(
            command.orderId(),
            command.currency(),
            idempotencyKey,
            null,  // Use default successUrl from config
            null); // Use default cancelUrl from config
        var gatewayResponse = paymentGatewayAdapter.createCheckoutSession(gatewayCommand);

        LOGGER.info("[PaymentRetryService] New Checkout Session created: {} (PaymentIntent: {})",
            gatewayResponse.sessionId(), gatewayResponse.paymentIntentId());

        // Update the Payment with the new transaction ID
        var updateCommand = new UpdatePaymentCommand(
            command.paymentId(),
            command.orderId(),
            gatewayResponse.paymentIntentId(),
            new java.util.Date(),
            PaymentStatus.PENDING,  // Reset to PENDING for retry
            null);  // Clear payment method

        payment.updatePayment(updateCommand);
        paymentRepository.save(payment);

        LOGGER.info("[PaymentRetryService] Payment updated with new transaction ID: {}",
            gatewayResponse.paymentIntentId());

        // Publish event for audit trail
        eventPublisher.publishEvent(new PaymentRetryInitiatedEvent(
            this,
            command.paymentId(),
            command.orderId(),
            gatewayResponse.paymentIntentId(),
            command.currency()));

        return new PaymentRetryResponse(
            payment.getId(),
            gatewayResponse.checkoutUrl(),
            gatewayResponse.paymentIntentId());
    }

    /**
     * Response from payment retry operation
     */
    public record PaymentRetryResponse(
        Long paymentId,
        String checkoutUrl,
        String newTransactionId) {}
}
