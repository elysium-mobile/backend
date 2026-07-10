package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;


/**
 * StripeCheckoutResponse
 * Response body for POST /api/v1/payments/stripe/checkout.
 * Returns the hosted Stripe Checkout Session URL that the frontend
 * should redirect the customer to. The entire payment flow (card
 * details, confirmation) is handled by Stripe on its hosted page.
 *
 * @param checkoutUrl the URL to redirect the customer to Stripe's hosted checkout page
 * @param sessionId   the Stripe Checkout Session ID (cs_...)
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record StripeCheckoutResponse(
        String checkoutUrl,
        String sessionId
) {}
