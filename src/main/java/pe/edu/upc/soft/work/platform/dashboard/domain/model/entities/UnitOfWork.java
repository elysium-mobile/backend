package pe.edu.upc.soft.work.platform.dashboard.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.ArrayList;
import java.util.List;


/**
 * UnitOfWork aggregate root entity.
 */
@Entity
@Table(name = "unit_of_work")
public class UnitOfWork extends AuditableAbstractAggregateRoot<UnitOfWork> {

    @Getter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Column(name = "work_team_list", nullable = true)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkTeam> workTeamList;

    /**
     * Default constructor for JPA.
     */
    public UnitOfWork() {}

    /**
     * Constructor to create a UnitOfWork from a CreateUnitOfWorkCommand.
     * @param command the command containing unitofwork details
     */
    public UnitOfWork(CreateUnitOfWorkCommand command) {
        this.name = command.name();
        this.workTeamList=command.workTeamList();
    }

    /**
     * Updates the UnitOfWork with details from an UpdateUnitOfWorkCommand.
     * @param command the command containing updated unitofwork details
     */
    public void updateUnitOfWork(UpdateUnitOfWorkCommand command) {
        this.name = command.name();
    }

    public void addWorkTeam(WorkTeam workTeam) {
        if (this.workTeamList == null) {
            this.workTeamList = new ArrayList<>();
        }
        boolean alreadyExists = this.workTeamList.stream()
                .anyMatch(w -> w.getId().equals(workTeam.getId()));
        if (alreadyExists) {
            throw new IllegalStateException(
                    "WorkTeam with ID " + workTeam.getId() + " is already assigned to this unit of work.");
        }
        this.workTeamList.add(workTeam);
    }
}
