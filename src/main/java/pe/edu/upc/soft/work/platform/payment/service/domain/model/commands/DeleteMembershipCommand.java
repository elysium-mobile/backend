package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

/**
 * Command to delete a Membership
 */
public record DeleteMembershipCommand(Long membershipId) {

    /**
     * Constructor with validation
     */
    public DeleteMembershipCommand {
        if (membershipId == null || membershipId <= 0) {
            throw new IllegalArgumentException("[DeleteMembershipCommand] membershipId must be a positive number");
        }
    }
}
