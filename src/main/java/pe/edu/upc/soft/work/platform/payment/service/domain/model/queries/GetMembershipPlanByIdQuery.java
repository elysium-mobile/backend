package pe.edu.upc.soft.work.platform.payment.service.domain.model.queries;

/**
 * Query to retrieve a MembershipPlan by their unique identifier.
 */
public record GetMembershipPlanByIdQuery(Long membershipplanId) {

    /**
     * Constructor to validate the membershipplanId parameter.
     */
    public GetMembershipPlanByIdQuery {
        if (membershipplanId == null || membershipplanId <= 0) {
            throw new IllegalArgumentException("MembershipPlan ID must be a positive number.");
        }
    }
}
