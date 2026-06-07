package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.AreaCompanyId;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;

import java.util.Date;
import java.util.Objects;

public record UpdateReportCommand(Long reportId,
                                  String reason,
                                  String description,
                                  UserAccountId userAccountId,
                                  Date reportDate,
                                  AreaCompanyId areaCompanyId) {

    public UpdateReportCommand{
        Objects.requireNonNull(reportId, "[UpdateReportCommand] reportId must not be null");
        Objects.requireNonNull(reason, "[CreateReportCommand] reason must not be null");
        Objects.requireNonNull(description, "[CreateReportCommand] description must not be null");
        Objects.requireNonNull(userAccountId, "[CreateReportCommand] userAccountId must not be null");
        Objects.requireNonNull(reportDate, "[CreateReportCommand] reportDate must not null");
        Objects.requireNonNull(areaCompanyId, "[CreateReportCommand] areaCompanyId must not be null");

    }
}
