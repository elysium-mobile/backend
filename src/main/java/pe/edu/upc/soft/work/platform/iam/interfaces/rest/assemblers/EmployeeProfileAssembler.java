package pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateEmployeeProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateEmployeeProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.EmployeeProfile;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.WorkOfTeamId;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.CreateEmployeeProfileRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.EmployeeProfileResponse;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UpdateEmployeeProfileRequest;

public class EmployeeProfileAssembler {

    /**
     * Converts a CreateEmployeeProfileRequest into a CreateEmployeeProfileCommand.
     * @param request The create employee profile request.
     * @return The corresponding CreateEmployeeProfileCommand.
     */
    public static CreateEmployeeProfileCommand toCommandFromRequest(CreateEmployeeProfileRequest request) {
        return new CreateEmployeeProfileCommand(
                request.dateStart(),
                request.position(),
                request.salary(),
                request.UserAccountId(),
                new WorkOfTeamId(request.workOfTeamId())
        );
    }


    /**
     * Converts an UpdateEmployeeProfileRequest into an UpdateEmployeeProfileCommand.
     * @param employeeProfileId The employee profile ID to update.
     * @param request The update employee profile request.
     * @return The corresponding UpdateEmployeeProfileCommand.
     */
    public static UpdateEmployeeProfileCommand toCommandFromRequest(Long employeeProfileId, UpdateEmployeeProfileRequest request) {
        return new UpdateEmployeeProfileCommand(
                employeeProfileId,
                request.dateStart(),
                request.position(),
                request.salary(),
                new WorkOfTeamId(request.workOfTeamId())
        );

    }

    /**
     * Converts an EmployeeProfile entity into an EmployeeProfileResponse.
     * @param entity The employee profile entity.
     * @return The corresponding EmployeeProfileResponse.
     */
    public static EmployeeProfileResponse toResponseFromEntity(EmployeeProfile entity) {
        return new EmployeeProfileResponse(
                entity.getId(),
                entity.getDateStart(),
                entity.getPosition(),
                entity.getSalary(),
                entity.getUserAccountId(),
                entity.getWorkOfTeamId().workOfTeamId()
        );
    }

}
