package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

/**
 *  Response object for a WorkTeam, containing its ID, name, leader, and associated unit of work ID.
 * @param workTeamId  the unique identifier of the work team
 * @param teamName  the name of the work team
 * @param leaderOfTeam  the name of the leader of the work team
 * @param unitOfWorkId  the unique identifier of the unit of work associated with the work team
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WorkTeamResponse(
        Long workTeamId,
        String teamName,
        String leaderOfTeam,
        Long unitOfWorkId
) {
}
