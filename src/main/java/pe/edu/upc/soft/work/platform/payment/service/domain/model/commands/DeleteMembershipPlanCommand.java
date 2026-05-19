package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

/**
 * Command to delete a MembershipPlan
 */
public record DeleteMembershipPlanCommand(Long membershipplanId) {

    /**
     * Constructor with validation
     */
    public DeleteMembershipPlanCommand {
        if (membershipplanId == null || membershipplanId <= 0) {
            throw new IllegalArgumentException("[DeleteMembershipPlanCommand] membershipplanId must be a positive number");
        }
    }
}
