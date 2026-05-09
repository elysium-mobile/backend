package pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateDashboardCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


/**
 * Dashboard aggregate root entity.
 */
@Entity
public class Dashboard extends AuditableAbstractAggregateRoot<Dashboard> {

    @Getter
    private Long ruc;

    /**
     * Default constructor for JPA.
     */
    public Dashboard() {}

    /**
     * Constructor to create a Dashboard from a CreateDashboardCommand.
     * @param command the command containing dashboard details
     */
    public Dashboard(CreateDashboardCommand command) {
        this.ruc = command.ruc();
    }

    /**
     * Updates the Dashboard with details from an UpdateDashboardCommand.
     * @param command the command containing updated dashboard details
     */
    public void updateDashboard(UpdateDashboardCommand command) {
        this.ruc = command.ruc();
    }
}
