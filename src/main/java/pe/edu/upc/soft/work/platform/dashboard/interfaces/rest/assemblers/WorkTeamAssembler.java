package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateWorkTeamCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateWorkTeamCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.WorkTeam;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateWorkTeamRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateWorkTeamRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.WorkTeamResponse;

public class WorkTeamAssembler {

    /**
     *  Converts a CreateWorkTeamRequest to a CreateWorkTeamCommand.
     */
    public static CreateWorkTeamCommand toCommandFromRequest(CreateWorkTeamRequest request) {
        return new CreateWorkTeamCommand(request.teamName(), request.leaderOfTeam());
    }

    /**
     *  Converts an UpdateWorkTeamRequest to an UpdateWorkTeamCommand.
     */
    public static UpdateWorkTeamCommand toCommandFromRequest(Long workTeamId, UpdateWorkTeamRequest request) {
        return new UpdateWorkTeamCommand(workTeamId, request.teamName(), request.leaderOfTeam());
    }

    /**
     *  Converts a WorkTeam entity to a WorkTeamResponse.
     */
    public static WorkTeamResponse toResponseFromEntity(WorkTeam workTeam)
    {
        return new WorkTeamResponse(workTeam.getId(), workTeam.getTeamName(), workTeam.getLeaderOfTeam());
    }
}
