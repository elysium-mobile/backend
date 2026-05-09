package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Company
 */
public record UpdateCompanyCommand(Long companyId, String name, String RUC, String contactEmail, String contactPhone) {

    /**
     * Constructor with validation
     */
    public UpdateCompanyCommand {
        Objects.requireNonNull(companyId, "[UpdateCompanyCommand] companyId must not be null");
    }
}
