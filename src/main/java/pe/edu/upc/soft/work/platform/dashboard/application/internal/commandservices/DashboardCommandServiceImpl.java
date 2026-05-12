package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Dashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.DashboardCommandService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.DashboardRepository;

import java.util.Optional;

/**
 * Service implementation for handling Dashboard commands.
 */
@Service
public class DashboardCommandServiceImpl implements DashboardCommandService {
    private final DashboardRepository dashboardRepository;

    /**
     * Constructor for DashboardCommandServiceImpl.
     * @param dashboardRepository the repository for Dashboard persistence
     */
    public DashboardCommandServiceImpl(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    /**
     * Handles the creation of a new Dashboard.
     * @param command the command to create a Dashboard
     * @return the generated ID of the new Dashboard
     */
    @Override
    public Long handle(CreateDashboardCommand command) {
        var dashboard = new Dashboard(command);
        try {
            dashboardRepository.save(dashboard);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Dashboard: " + e.getMessage(), e);
        }
        return dashboard.getId();
    }

    /**
     * Handles the update of an existing Dashboard.
     * @param command the command to update a Dashboard
     * @return the updated Dashboard as an Optional
     */
    @Override
    public Optional<Dashboard> handle(UpdateDashboardCommand command) {
        var dashboardId = command.dashboardId();
        if (!this.dashboardRepository.existsById(dashboardId)) {
            throw new RuntimeException("Dashboard with ID " + dashboardId + " does not exist.");
        }

        var dashboardToUpdate = this.dashboardRepository.findById(dashboardId).get();
        dashboardToUpdate.updateDashboard(command);
        try {
            var updatedDashboard = this.dashboardRepository.save(dashboardToUpdate);
            return Optional.of(updatedDashboard);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Dashboard: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of a Dashboard.
     * @param command the command to delete a Dashboard
     */
    @Override
    public void handle(DeleteDashboardCommand command) {
        if (!dashboardRepository.existsById(command.dashboardId())) {
            throw new RuntimeException("Dashboard with ID " + command.dashboardId() + " does not exist.");
        }
        try {
            dashboardRepository.deleteById(command.dashboardId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Dashboard: " + e.getMessage(), e);
        }
    }
}