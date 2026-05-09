package pe.edu.upc.soft.work.platform.payment.service.domain.model.queries;

/**
 * Query to retrieve a Membership by their unique identifier.
 */
public record GetMembershipByIdQuery(Long membershipId) {

    /**
     * Constructor to validate the membershipId parameter.
     */
    public GetMembershipByIdQuery {
        if (membershipId == null || membershipId <= 0) {
            throw new IllegalArgumentException("Membership ID must be a positive number.");
        }
    }
}
