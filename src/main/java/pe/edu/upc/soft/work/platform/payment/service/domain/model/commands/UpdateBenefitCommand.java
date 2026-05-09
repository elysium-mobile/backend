package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Benefit
 */
public record UpdateBenefitCommand(Long benefitId, String title, String description) {

    /**
     * Constructor with validation
     */
    public UpdateBenefitCommand {
        Objects.requireNonNull(benefitId, "[UpdateBenefitCommand] benefitId must not be null");
    }
}
