package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateStripeCheckoutCommand;

/**
 * StripeCheckoutCommandService
 * Domain service interface that defines the contract for creating a
 * Stripe PaymentIntent and handling the resulting webhook events.
 */
public interface StripeCheckoutCommandService {

    /**
     * Creates a Stripe PaymentIntent for the given Order and returns
     * the clientSecret that the frontend needs to confirm the payment.
     *
     * @param command the command carrying the Order ID and currency
     * @return the Stripe PaymentIntent clientSecret
     */
    String handle(CreateStripeCheckoutCommand command);
}
