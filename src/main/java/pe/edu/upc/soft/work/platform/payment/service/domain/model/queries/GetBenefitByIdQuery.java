package pe.edu.upc.soft.work.platform.payment.service.domain.model.queries;

/**
 * Query to retrieve a Benefit by their unique identifier.
 */
public record GetBenefitByIdQuery(Long benefitId) {

    /**
     * Constructor to validate the benefitId parameter.
     */
    public GetBenefitByIdQuery {
        if (benefitId == null || benefitId <= 0) {
            throw new IllegalArgumentException("Benefit ID must be a positive number.");
        }
    }
}
