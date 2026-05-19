package pe.edu.upc.soft.work.platform.dashboard.domain.model.queries;

/**
 * Query to retrieve a WorkTeam by their unique identifier.
 */
public record GetWorkTeamByIdQuery(Long workteamId) {

    /**
     * Constructor to validate the workteamId parameter.
     */
    public GetWorkTeamByIdQuery {
        if (workteamId == null || workteamId <= 0) {
            throw new IllegalArgumentException("WorkTeam ID must be a positive number.");
        }
    }
}
