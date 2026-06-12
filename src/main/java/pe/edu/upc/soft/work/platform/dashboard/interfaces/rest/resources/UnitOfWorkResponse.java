package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.WorkTeam;

import java.util.List;

public record UnitOfWorkResponse(
        Long unitOfWorkId,
        String name,
        List<WorkTeamResponse> workTeamList
) {
}
