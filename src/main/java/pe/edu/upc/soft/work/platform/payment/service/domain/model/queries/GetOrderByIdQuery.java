package pe.edu.upc.soft.work.platform.payment.service.domain.model.queries;

/**
 * Query to retrieve a Order by their unique identifier.
 */
public record GetOrderByIdQuery(Long orderId) {

    /**
     * Constructor to validate the orderId parameter.
     */
    public GetOrderByIdQuery {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be a positive number.");
        }
    }
}
