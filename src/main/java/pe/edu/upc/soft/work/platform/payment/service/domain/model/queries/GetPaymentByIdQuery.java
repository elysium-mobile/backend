package pe.edu.upc.soft.work.platform.payment.service.domain.model.queries;

/**
 * Query to retrieve a Payment by their unique identifier.
 */
public record GetPaymentByIdQuery(Long paymentId) {

    /**
     * Constructor to validate the paymentId parameter.
     */
    public GetPaymentByIdQuery {
        if (paymentId == null || paymentId <= 0) {
            throw new IllegalArgumentException("Payment ID must be a positive number.");
        }
    }
}
