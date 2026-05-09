package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

/**
 * Command to delete a Benefit
 */
public record DeleteBenefitCommand(Long benefitId) {

    /**
     * Constructor with validation
     */
    public DeleteBenefitCommand {
        if (benefitId == null || benefitId <= 0) {
            throw new IllegalArgumentException("[DeleteBenefitCommand] benefitId must be a positive number");
        }
    }
}
