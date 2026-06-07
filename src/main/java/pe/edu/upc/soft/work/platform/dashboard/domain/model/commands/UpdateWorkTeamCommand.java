package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing WorkTeam
 */
public record UpdateWorkTeamCommand(Long workteamId, String teamName, String leaderOfTeam, Long unitOfWorkId) {

    /**
     * Constructor with validation
     */
    public UpdateWorkTeamCommand {
        Objects.requireNonNull(workteamId, "[UpdateWorkTeamCommand] workteamId must not be null");
    }
}
