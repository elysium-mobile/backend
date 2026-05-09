package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new MembershipPlan
 */
public record CreateMembershipPlanCommand(String planName) {

    /**
     * Constructor with validation
     */
    public CreateMembershipPlanCommand {
        Objects.requireNonNull(planName, "[CreateMembershipPlanCommand] planName must not be null");
    }
}
