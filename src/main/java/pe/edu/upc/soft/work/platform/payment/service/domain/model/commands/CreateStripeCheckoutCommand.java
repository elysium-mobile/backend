package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

/**
 * CreateStripeCheckoutCommand
 */
public record CreateStripeCheckoutCommand(
        Long orderId,
        String currency,
        String idempotencyKey

) {

  public CreateStripeCheckoutCommand(Long orderId, String currency) {
    this(orderId, currency, "checkout-order-" + orderId);
  }
}
