package pe.edu.upc.soft.work.platform.payment.service.application.internal.retries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateStripeCheckoutCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.RetryPaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.PaymentRetryInitiatedEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.PaymentStatus;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentGatewayAdapter;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.PaymentRepository;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

/**
 * PaymentRetryService
 * Handles logic for retrying failed payments by creating a new Stripe PaymentIntent
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
     * Retries a failed payment by creating a new Stripe PaymentIntent.
     * Updates the original Payment record with the new transaction ID.
     *
     * @param command the retry payment command
     * @return PaymentRetryResponse with new clientSecret for payment confirmation
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

        // Create a new PaymentIntent through the gateway adapter.
        // IMPORTANT: this must use a DIFFERENT idempotency key than the original
        // checkout (retry-payment-{paymentId}-{uuid}, not checkout-order-{orderId}).
        // Reusing the checkout key here would make Stripe return the same, already
        // failed PaymentIntent instead of creating a fresh one to retry with.
        var idempotencyKey = "retry-payment-" + command.paymentId() + "-" + java.util.UUID.randomUUID();
        var gatewayCommand = new CreateStripeCheckoutCommand(command.orderId(), command.currency(), idempotencyKey);
        var gatewayResponse = paymentGatewayAdapter.createPaymentIntent(gatewayCommand);

        LOGGER.info("[PaymentRetryService] New PaymentIntent created: {}", gatewayResponse.transactionId());

        // Update the Payment with the new transaction ID
        var updateCommand = new UpdatePaymentCommand(
            command.paymentId(),
            command.orderId(),
            gatewayResponse.transactionId(),
            new java.util.Date(),
            PaymentStatus.PENDING,  // Reset to PENDING for retry
            null);  // Clear payment method

        payment.updatePayment(updateCommand);
        paymentRepository.save(payment);

        LOGGER.info("[PaymentRetryService] Payment updated with new transaction ID: {}",
            gatewayResponse.transactionId());

        // Publish event for audit trail
        eventPublisher.publishEvent(new PaymentRetryInitiatedEvent(
            this,
            command.paymentId(),
            command.orderId(),
            gatewayResponse.transactionId(),
            command.currency()));

        return new PaymentRetryResponse(
            payment.getId(),
            gatewayResponse.clientSecret(),
            gatewayResponse.transactionId());
    }

    /**
     * Response from payment retry operation
     */
    public record PaymentRetryResponse(
        Long paymentId,
        String clientSecret,
        String newTransactionId) {}
}
