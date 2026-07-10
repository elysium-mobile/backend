package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

/**
 * CreateStripeCheckoutSessionCommand
 * Command used to create a Stripe Checkout Session for a given Order.
 * Carries the order identifier, currency, idempotency key, and the
 * success/cancel URLs that Stripe will redirect the customer to.
 *
 * @param orderId        the internal Order ID being paid for
 * @param currency       the three-letter ISO currency code (e.g. "usd")
 * @param idempotencyKey unique key to guarantee idempotent creation
 * @param successUrl     URL to redirect the customer after successful payment
 * @param cancelUrl      URL to redirect the customer if payment is cancelled
 */
public record CreateStripeCheckoutSessionCommand(
    Long orderId,
    String currency,
    String idempotencyKey,
    String successUrl,
    String cancelUrl
) {

  /**
   * Convenience constructor that derives a default idempotency key
   * from the order ID.
   */
  public CreateStripeCheckoutSessionCommand(Long orderId, String currency, String successUrl, String cancelUrl) {
    this(orderId, currency, "checkout-session-" + orderId, successUrl, cancelUrl);
  }
}
