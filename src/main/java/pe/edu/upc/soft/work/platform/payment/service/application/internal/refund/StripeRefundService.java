package pe.edu.upc.soft.work.platform.payment.service.application.internal.refund;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.net.RequestOptions;
import com.stripe.param.RefundCreateParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.InitiateRefundCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.RefundInitiatedEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.RefundService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.PaymentRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.stripe.StripeProperties;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

/**
 * StripeRefundService
 * Implementation of RefundService for Stripe payment provider.
 * Handles refund operations through Stripe API.
 */
@Service
public class StripeRefundService implements RefundService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StripeRefundService.class);

    private final PaymentRepository paymentRepository;
    private final StripeProperties stripeProperties;
    private final ApplicationEventPublisher eventPublisher;

    public StripeRefundService(PaymentRepository paymentRepository,
                               StripeProperties stripeProperties,
                               ApplicationEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.stripeProperties = stripeProperties;
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct
    public void initStripe() {
        Stripe.apiKey = stripeProperties.getSecretKey();
    }

    /**
     * Initiates a refund for a previously succeeded payment.
     * Can refund the full amount or a partial amount.
     *
     * @param command the refund command containing payment and amount details
     * @return RefundResponse with refund ID and status
     * @throws NotFoundArgumentException if the payment is not found
     * @throws RuntimeException if Stripe API call fails
     */
    @Override
    @Transactional
    public RefundResponse initiateRefund(InitiateRefundCommand command) {
        LOGGER.info("[StripeRefundService] Initiating refund for Payment ID: {} (Order: {})",
            command.paymentId(), command.orderId());

        // Validate payment exists and is succeeded
        var payment = paymentRepository.findById(command.paymentId())
            .orElseThrow(() -> new NotFoundArgumentException(
                String.format("[StripeRefundService] Payment ID: %s not found",
                    command.paymentId())));

        if (!payment.isSucceeded()) {
            throw new IllegalStateException(
                String.format("[StripeRefundService] Cannot refund Payment in status: %s",
                    payment.getPaymentStatus()));
        }

        try {
            var params = RefundCreateParams.builder()
                .setPaymentIntent(payment.getTransactionId());

            // Set partial refund amount if provided
            if (command.refundAmountCents() != null) {
                params.setAmount(Long.valueOf(command.refundAmountCents()));
            }

            // Add reason and metadata
            if (command.reason() != null) {
                params.setReason(RefundCreateParams.Reason.valueOf(command.reason()));
            }

            params.putMetadata("orderId", String.valueOf(command.orderId()));
            params.putMetadata("paymentId", String.valueOf(command.paymentId()));

            var requestOptions = RequestOptions.builder()
                .setIdempotencyKey(command.idempotencyKey())
                .build();

            var refund = Refund.create(params.build(), requestOptions);

            LOGGER.info("[StripeRefundService] Refund created with ID: {} (Status: {})",
                refund.getId(), refund.getStatus());

            // Publish event for async handling
            eventPublisher.publishEvent(new RefundInitiatedEvent(
                this,
                refund.getId(),
                command.paymentId(),
                command.orderId(),
                Math.toIntExact(refund.getAmount()),
                command.reason()));

            return new RefundResponse(
                refund.getId(),
                refund.getPaymentIntent(),
                Math.toIntExact(refund.getAmount()),
                refund.getStatus());

        } catch (StripeException e) {
            LOGGER.error("[StripeRefundService] Failed to create refund: {}", e.getMessage(), e);
            throw new RuntimeException(
                "[StripeRefundService] Failed to create Stripe refund: " + e.getMessage(), e);
        }
    }
}
