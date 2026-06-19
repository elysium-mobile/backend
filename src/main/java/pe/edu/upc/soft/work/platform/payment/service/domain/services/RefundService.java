package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.InitiateRefundCommand;

/**
 * RefundService Port/Interface
 * Defines contract for refund operations with payment providers (Stripe, PayPal, etc.)
 */
public interface RefundService {

    /**
     * Initiates a refund for a previously succeeded payment intent.
     * Can refund full amount or partial amount.
     *
     * @param command the command containing payment and refund details
     * @return RefundResponse with refund ID and status
     */
    RefundResponse initiateRefund(InitiateRefundCommand command);

    /**
     * Response object from payment provider after initiating refund
     */
    record RefundResponse(
            String refundId,           // Provider's refund transaction ID (e.g., "re_123abc")
            String paymentIntentId,    // Original payment intent ID
            Integer refundedAmountCents,  // Amount refunded in cents
            String status              // Refund status (e.g., "succeeded", "pending")
    ) {}
}
