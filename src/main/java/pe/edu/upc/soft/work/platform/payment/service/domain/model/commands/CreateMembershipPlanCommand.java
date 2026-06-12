package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Benefit;

import java.util.List;
import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new MembershipPlan
 */
public record CreateMembershipPlanCommand(String planName, Integer price, List<Benefit> benefits, Long membershipId) {

    /**
     * Constructor with validation
     */
    public CreateMembershipPlanCommand {
        Objects.requireNonNull(planName, "[CreateMembershipPlanCommand] planName must not be null");
        Objects.requireNonNull(price,"[CreateMembershipPlanCommand] price must not be null");
    }
}
