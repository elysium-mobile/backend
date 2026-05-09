package pe.edu.upc.soft.work.platform.dashboard.domain.model.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


/**
 * UnitOfWork aggregate root entity.
 */
@Entity
public class UnitOfWork extends AuditableAbstractAggregateRoot<UnitOfWork> {

    @Getter
    private String name;

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
    }

    /**
     * Updates the UnitOfWork with details from an UpdateUnitOfWorkCommand.
     * @param command the command containing updated unitofwork details
     */
    public void updateUnitOfWork(UpdateUnitOfWorkCommand command) {
        this.name = command.name();
    }
}
