package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Report;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateReportCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateReportCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.AreaCompanyId;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateReportRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.ReportResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateReportRequest;

public class ReportAssembler{


    /**
     *  Converts a CreateReportRequest to a CreateReportCommand.
     */
    public static CreateReportCommand toCommandFromRequest(CreateReportRequest request) {
        return new CreateReportCommand(request.reason(), request.description(),new UserAccountId(request.userAccountId()),
                request.reportDate(),new AreaCompanyId(request.areaCompanyId()));
    }


    /**
     *  Converts an UpdateReportRequest to an UpdateReportCommand.
     */
    public static UpdateReportCommand toCommandFromRequest(Long reportId,
                                                           UpdateReportRequest request){
        return new UpdateReportCommand(reportId, request.reason(), request.description(),new UserAccountId(request.userAccountId()),
                request.reportDate(),new AreaCompanyId(request.areaCompanyId()));
    }

    /**
     *  Converts a Report entity to a ReportResponse.
     */
    public static ReportResponse toResponseFromEntity(Report report){
        return new ReportResponse(report.getId(), report.getReason(), report.getDescription(),
                report.getUserAccountId().userAccountId(),
                report.getReportDate(), report.getAreaCompanyId().areaCompanyId());
    }
}
