package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Company
 */
public record CreateCompanyCommand(String name, String RUC, String contactEmail, String contactPhone) {

    /**
     * Constructor with validation
     */
    public CreateCompanyCommand {
        Objects.requireNonNull(name, "[CreateCompanyCommand] name must not be null");
        Objects.requireNonNull(RUC, "[CreateCompanyCommand] RUC must not be null");
        Objects.requireNonNull(contactEmail, "[CreateCompanyCommand] contactEmail must not be null");
        Objects.requireNonNull(contactPhone, "[CreateCompanyCommand] contactPhone must not be null");
    }
}
