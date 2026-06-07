package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing MembershipPlan
 */
public record UpdateMembershipPlanCommand(Long membershipplanId, String planName, Integer price, Long membershipId) {

    /**
     * Constructor with validation
     */
    public UpdateMembershipPlanCommand {
        Objects.requireNonNull(membershipplanId, "[UpdateMembershipPlanCommand] membershipplanId must not be null");
        Objects.requireNonNull(price,"[CreateMembershipPlanCommand] price must not be null");
        Objects.requireNonNull(planName,"[CreateMembershipPlanCommand] plan Name must not be null");
    }
}
