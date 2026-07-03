package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;


/**
 * RRHHProfileResponse record to represent RRHH profile data in API responses.
 * @param RRHHDepartment the department of the RRHH profile
 * @param statusHierarchy the status hierarchy of the RRHH profile
 * @param userAccountId the unique identifier of the user account associated with the RRHH profile
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RRHHProfileResponse(

        Long rrhhProfileId,

        String RRHHDepartment,

        String statusHierarchy,

        Long userAccountId

) {
}
