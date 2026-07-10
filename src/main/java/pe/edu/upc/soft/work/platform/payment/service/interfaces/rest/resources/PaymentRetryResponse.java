package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

/**
 * PaymentRetryResponse
 * Response body for POST /api/v1/payments/stripe/{paymentId}/retry.
 * Returns the new checkout URL that the frontend should redirect to
 * for the retry attempt, along with the updated transaction ID.
 *
 * @param paymentId        the internal Payment ID being retried
 * @param checkoutUrl      the hosted Stripe Checkout URL for the retry
 * @param newTransactionId the new PaymentIntent ID created for the retry
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PaymentRetryResponse(
    Long paymentId,
    String checkoutUrl,
    String newTransactionId
) {
}
