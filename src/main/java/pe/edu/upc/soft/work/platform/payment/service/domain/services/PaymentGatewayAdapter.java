package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateStripeCheckoutSessionCommand;

/**
 * PaymentGatewayAdapter
 * Port/interface for integrating with external payment providers (Stripe, PayPal, etc.)
 * This abstraction allows the domain layer to remain independent of specific payment gateway implementations.
 */
public interface PaymentGatewayAdapter {

    /**
     * Creates a Stripe Checkout Session for the given Order and returns the
     * hosted checkout URL, session ID, and the underlying PaymentIntent ID.
     * The frontend redirects the customer to checkoutUrl; Stripe handles
     * the entire payment flow on its hosted page.
     *
     * @param command the command carrying the Order ID, currency, and redirect URLs
     * @return CheckoutSessionResponse containing the hosted checkout URL and identifiers
     */
    CheckoutSessionResponse createCheckoutSession(CreateStripeCheckoutSessionCommand command);

    /**
     * Response object from the payment gateway after creating a Checkout Session.
     *
     * @param checkoutUrl    the hosted Stripe Checkout page URL (frontend redirects here)
     * @param sessionId      the Stripe Checkout Session ID (cs_...)
     * @param paymentIntentId the PaymentIntent ID created under the hood by the session
     */
    record CheckoutSessionResponse(
            String checkoutUrl,
            String sessionId,
            String paymentIntentId
    ) {}
}
