package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing AreaCompany
 */
public record UpdateAreaCompanyCommand(Long areacompanyId, String name, Integer annualBudget) {

    /**
     * Constructor with validation
     */
    public UpdateAreaCompanyCommand {
        Objects.requireNonNull(areacompanyId, "[UpdateAreaCompanyCommand] areacompanyId must not be null");
    }
}
