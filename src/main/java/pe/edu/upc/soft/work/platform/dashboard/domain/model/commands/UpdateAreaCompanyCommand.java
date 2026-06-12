package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import java.util.Objects;

/**
 * Command to update an existing AreaCompany
 */
public record UpdateAreaCompanyCommand(Long areaCompanyId, String name, Integer annualBudget, Long companyId) {

    /**
     * Constructor with validation
     */
    public UpdateAreaCompanyCommand {
        Objects.requireNonNull(areaCompanyId, "[UpdateAreaCompanyCommand] areacompanyId must not be null");
    }
}
