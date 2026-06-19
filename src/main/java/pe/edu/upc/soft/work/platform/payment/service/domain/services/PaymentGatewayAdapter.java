package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateStripeCheckoutCommand;

/**
 * PaymentGatewayAdapter
 * Port/interface for integrating with external payment providers (Stripe, PayPal, etc.)
 * This abstraction allows the domain layer to remain independent of specific payment gateway implementations.
 */
public interface PaymentGatewayAdapter {

    /**
     * Creates a payment intent for the given Order and returns response with clientSecret and transactionId.
     * This method should be implemented by concrete adapters (e.g., StripePaymentGatewayAdapter).
     *
     * @param command the command carrying the Order ID and currency
     * @return PaymentGatewayResponse containing clientSecret and transactionId
     */
    PaymentGatewayResponse createPaymentIntent(CreateStripeCheckoutCommand command);

    /**
     * Response object from payment gateway after creating a payment intent
     */
    record PaymentGatewayResponse(
            String clientSecret,        // Client secret for frontend to confirm payment
            String transactionId,       // Gateway's transaction/payment intent ID
            String paymentMethod        // Type of payment method (e.g., "card", "bank_account")
    ) {}
}
