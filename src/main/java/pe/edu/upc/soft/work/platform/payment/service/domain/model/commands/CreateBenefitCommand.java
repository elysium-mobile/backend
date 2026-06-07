package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Benefit
 */
public record CreateBenefitCommand(String title, String description, Long membershipPlanId) {

    /**
     * Constructor with validation
     */
    public CreateBenefitCommand {
        Objects.requireNonNull(title, "[CreateBenefitCommand] title must not be null");
        Objects.requireNonNull(description, "[CreateBenefitCommand] description must not be null");
    }
}
