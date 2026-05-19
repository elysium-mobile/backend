package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

/**
 * Command to delete a WorkTeam
 */
public record DeleteWorkTeamCommand(Long workteamId) {

    /**
     * Constructor with validation
     */
    public DeleteWorkTeamCommand {
        if (workteamId == null || workteamId <= 0) {
            throw new IllegalArgumentException("[DeleteWorkTeamCommand] workteamId must be a positive number");
        }
    }
}
