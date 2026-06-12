package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new WorkTeam
 */
public record CreateWorkTeamCommand(String teamName, String leaderOfTeam, Long unitOfWorkId) {

    /**
     * Constructor with validation
     */
    public CreateWorkTeamCommand {
        Objects.requireNonNull(teamName, "[CreateWorkTeamCommand] teamName must not be null");
        Objects.requireNonNull(leaderOfTeam, "[CreateWorkTeamCommand] leaderOfTeam must not be null");
    }
}
