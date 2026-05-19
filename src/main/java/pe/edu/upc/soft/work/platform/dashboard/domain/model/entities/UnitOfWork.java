package pe.edu.upc.soft.work.platform.dashboard.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


/**
 * UnitOfWork aggregate root entity.
 */
@Entity
@Table(name = "unit_of_work")
public class UnitOfWork extends AuditableAbstractAggregateRoot<UnitOfWork> {

    @Getter
    @Column(name = "name", nullable = false)
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
