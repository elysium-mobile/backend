package pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.RRHHProfile;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.CreateRRHHProfileRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.RRHHProfileResponse;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UpdateRRHHProfileRequest;

public class RRHHProfileAssembler {

    /**
     * Converts a CreateRRHHProfileRequest into a CreateRRHHProfileCommand.
     * @param request the request containing the information to create a new RRHH profile
     * @return a CreateRRHHProfileCommand with the information from the request
     */
    public static CreateRRHHProfileCommand toCommandFromRequest(CreateRRHHProfileRequest request) {
        return new CreateRRHHProfileCommand(
                request.RRHHDepartment(),
                request.statusHierarchy(),
                request.userAccountId()
        );
    }

    /**
     * Converts an UpdateRRHHProfileRequest into an UpdateRRHHProfileCommand.
     * @param rrhhProfileId the ID of the RRHH profile to update
     * @param request the request containing the information to update the RRHH profile
     * @return an UpdateRRHHProfileCommand with the information from the request and the specified RRHH profile ID
     */
    public static UpdateRRHHProfileCommand toCommandFromRequest(Long rrhhProfileId, UpdateRRHHProfileRequest request) {
        return new UpdateRRHHProfileCommand(
                rrhhProfileId,
                request.RRHHDepartment(),
                request.statusHierarchy()
        );
    }

    /**
     * Converts an RRHHProfile entity into an RRHHProfileResponse.
     * @param rrhhProfile the RRHHProfile entity to convert
     * @return an RRHHProfileResponse containing the information from the RRHHProfile entity
     */
    public static RRHHProfileResponse toResponseFromEntity(RRHHProfile rrhhProfile){
        return new RRHHProfileResponse(
                rrhhProfile.getId(),
                rrhhProfile.getRRHHDepartment(),
                rrhhProfile.getStatusHierarchy(),
                rrhhProfile.getUserAccountId()
        );
    }
}
