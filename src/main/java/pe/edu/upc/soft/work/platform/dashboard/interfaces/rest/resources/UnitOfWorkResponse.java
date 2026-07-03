package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.WorkTeam;

import java.util.List;

/**
 * Response DTO for UnitOfWork resource.
 * @param unitOfWorkId  the unique identifier of the UnitOfWork
 * @param name  the name of the UnitOfWork
 * @param workTeamList  the list of WorkTeams associated with this UnitOfWork
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UnitOfWorkResponse(
        Long unitOfWorkId,
        String name,
        List<WorkTeamResponse> workTeamList
) {
}
