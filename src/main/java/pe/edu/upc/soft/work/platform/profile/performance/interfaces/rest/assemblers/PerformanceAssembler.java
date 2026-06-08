package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.Performance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.AddCommentEmployeeToPerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.EmployeeProfileId;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class PerformanceAssembler {

    /**
     * Converts a CreatePerformanceRequest to a CreatePerformanceCommand.
     */
    public static CreatePerformanceCommand toCommandFromRequest(CreatePerformanceRequest request) {
        return new CreatePerformanceCommand(new EmployeeProfileId(request.employeeProfileId()), request.dateTime(), request.classification(),new ArrayList<>());
    }

    /**
     * Converts an UpdatePerformanceRequest to an UpdatePerformanceCommand.
     */
    public static UpdatePerformanceCommand toCommandFromRequest(Long performanceId, UpdatePerformanceRequest request) {
        return new UpdatePerformanceCommand(performanceId, new EmployeeProfileId(request.employeeProfileId()), request.dateTime(), request.classification());
    }

    public static AddCommentEmployeeToPerformanceCommand toCommandFromRequest(Long performanceId, AddCommentEmployeeToPerformanceRequest request){
        return new AddCommentEmployeeToPerformanceCommand(request.commentId(), performanceId);
    }
    /**
     * Converts a Performance entity to a PerformanceResponse.
     */
    public static PerformanceResponse toResponseFromEntity(Performance performance) {
        List<CommentEmployeeResponse> commentEmployeeResponseList = performance.getCommentEmployeeList().stream()
                .map(commentEmployee -> new CommentEmployeeResponse(
                        commentEmployee.getId(),
                        commentEmployee.getTitle(),
                        commentEmployee.getContent(),
                        commentEmployee.getRrhhProfileId().rrhhProfileId(),
                        commentEmployee.getPerformanceId()
                ))
                .toList();

        return new PerformanceResponse(performance.getId(), performance.getEmployeeProfileId().employeeProfileId(), performance.getDateTime(), performance.getClassification(), commentEmployeeResponseList);
    }
}
