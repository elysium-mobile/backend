package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

public record WorkTeamResponse(
        Long workTeamId,
        String teamName,
        String leaderOfTeam,
        Long unitOfWorkId
) {
}
