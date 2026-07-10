package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateStripeCheckoutSessionCommand;

/**
 * StripeCheckoutCommandService
 * Domain service interface that defines the contract for creating a
 * Stripe Checkout Session — the hosted payment page that Stripe
 * manages end-to-end.
 */
public interface StripeCheckoutCommandService {

    /**
     * Creates a Stripe Checkout Session for the given Order and returns
     * the hosted checkout URL that the frontend should redirect to.
     *
     * @param command the command carrying the Order ID, currency, and redirect URLs
     * @return the hosted Stripe Checkout page URL
     */
    String handle(CreateStripeCheckoutSessionCommand command);
}
