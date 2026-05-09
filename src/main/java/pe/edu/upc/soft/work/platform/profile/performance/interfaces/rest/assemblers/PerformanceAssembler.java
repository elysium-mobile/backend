package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.Performance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.CreatePerformanceRequest;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.UpdatePerformanceRequest;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.PerformanceResponse;

public class PerformanceAssembler {

    /**
     * Converts a CreatePerformanceRequest to a CreatePerformanceCommand.
     */
    public static CreatePerformanceCommand toCommandFromRequest(CreatePerformanceRequest request) {
        return new CreatePerformanceCommand(request.employeeProfileId(), request.dateTime(), request.classification());
    }

    /**
     * Converts an UpdatePerformanceRequest to an UpdatePerformanceCommand.
     */
    public static UpdatePerformanceCommand toCommandFromRequest(Long performanceId, UpdatePerformanceRequest request) {
        return new UpdatePerformanceCommand(performanceId, request.employeeProfileId(), request.dateTime(), request.classification());
    }

    /**
     * Converts a Performance entity to a PerformanceResponse.
     */
    public static PerformanceResponse toResponseFromEntity(Performance performance) {
        return new PerformanceResponse(performance.getId(), performance.getEmployeeProfileId(), performance.getDateTime(), performance.getClassification());
    }
}
