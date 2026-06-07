package pe.edu.upc.soft.work.platform.dashboard.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateWorkTeamCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateWorkTeamCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


/**
 * WorkTeam aggregate root entity.
 */
@Entity
@Table(name = "work_teams")
public class WorkTeam extends AuditableAbstractAggregateRoot<WorkTeam> {

    @Getter
    @Column(name = "team_name", nullable = false)
    private String teamName;
    @Getter
    @Column(name = "leader_of_team", nullable = false)
    private String leaderOfTeam;

    @Getter
    @Column(name = "unit_of_work_id", nullable = false)
    private Long unitOfWorkId;

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
        this.unitOfWorkId = command.unitOfWorkId();
    }

    /**
     * Updates the WorkTeam with details from an UpdateWorkTeamCommand.
     * @param command the command containing updated workteam details
     */
    public void updateWorkTeam(UpdateWorkTeamCommand command) {
        this.teamName = command.teamName();
        this.leaderOfTeam = command.leaderOfTeam();
        this.unitOfWorkId = command.unitOfWorkId();
    }
}
