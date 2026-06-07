package pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.Widget;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.List;


/**
 * Dashboard aggregate root entity.
 */
@Entity
@Table(name = "dashboards")
public class Dashboard extends AuditableAbstractAggregateRoot<Dashboard> {

    @Getter
    @Column(name = "RUC", nullable = false)
    private String ruc;

    @Getter
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "widgets", nullable = true)
    private List<Widget> widgets;

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
        this.widgets =command.widgetList();
    }

    /**
     * Updates the Dashboard with details from an UpdateDashboardCommand.
     * @param command the command containing updated dashboard details
     */
    public void updateDashboard(UpdateDashboardCommand command) {
        this.ruc = command.ruc();
    }
}
