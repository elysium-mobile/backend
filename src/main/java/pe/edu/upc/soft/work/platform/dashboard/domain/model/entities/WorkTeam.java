package pe.edu.upc.soft.work.platform.dashboard.domain.model.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateWorkTeamCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateWorkTeamCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


/**
 * WorkTeam aggregate root entity.
 */
@Entity
public class WorkTeam extends AuditableAbstractAggregateRoot<WorkTeam> {

    @Getter
    private String teamName;
    @Getter
    private String leaderOfTeam;

    /**
     * Default constructor for JPA.
     */
    public WorkTeam() {}

    /**
     * Constructor to create a WorkTeam from a CreateWorkTeamCommand.
     * @param command the command containing workteam details
     */
    public WorkTeam(CreateWorkTeamCommand command) {
        this.teamName = command.teamName();
        this.leaderOfTeam = command.leaderOfTeam();
    }

    /**
     * Updates the WorkTeam with details from an UpdateWorkTeamCommand.
     * @param command the command containing updated workteam details
     */
    public void updateWorkTeam(UpdateWorkTeamCommand command) {
        this.teamName = command.teamName();
        this.leaderOfTeam = command.leaderOfTeam();
    }
}
