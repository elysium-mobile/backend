package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new AreaCompany
 */
public record CreateAreaCompanyCommand(String name, Integer annualBudget) {

    /**
     * Constructor with validation
     */
    public CreateAreaCompanyCommand {
        Objects.requireNonNull(name, "[CreateAreaCompanyCommand] name must not be null");
        Objects.requireNonNull(annualBudget, "[CreateAreaCompanyCommand] annualBudget must not be null");
    }
}
