package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.AreaCompanyId;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;

import java.util.Date;
import java.util.Objects;

/**
 * Command to create a new Report
 * @param reason the reason for the report
 * @param description the description for the report
 * @param userAccountId the userAccountId for the report
 * @param reportDate the reportDate for the report
 * @param areaCompanyId the area Company for the report
 */
public record CreateReportCommand(String reason,
                                  String description,
                                  UserAccountId userAccountId,
                                  Date reportDate,
                                  AreaCompanyId areaCompanyId
) {

    public CreateReportCommand{
        Objects.requireNonNull(reason, "[CreateReportCommand] reason must not be null");
        Objects.requireNonNull(description, "[CreateReportCommand] description must not be null");
        Objects.requireNonNull(userAccountId, "[CreateReportCommand] userAccountId must not be null");
        Objects.requireNonNull(reportDate, "[CreateReportCommand] reportDate must not null");
        Objects.requireNonNull(areaCompanyId, "[CreateReportCommand] areaCompanyId must not be null");
    }
}
