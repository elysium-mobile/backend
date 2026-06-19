package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Dashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.AddWidgetToDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteDashboardCommand;

import java.util.Optional;

/**
 * Service interface for handling Dashboard-related commands.
 */
public interface DashboardCommandService {

    /**
     * Handles the creation of a new Dashboard.
     */
    Long handle(CreateDashboardCommand command);

    /**
     * Handles the update of an existing Dashboard.
     */
    Optional<Dashboard> handle(UpdateDashboardCommand command);

    /**
     * Handles the deletion of an existing Dashboard.
     */
    void handle(DeleteDashboardCommand command);

    /**
     *  Handles the addition of a Widget to a Dashboard.
     * @param command the command containing the details of the Widget to be added and the Dashboard it belongs to
     */
    void handle(AddWidgetToDashboardCommand command);
}
