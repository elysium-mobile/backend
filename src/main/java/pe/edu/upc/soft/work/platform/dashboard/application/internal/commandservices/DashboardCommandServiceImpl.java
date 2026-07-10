package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Dashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.AddWidgetToDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.DashboardCommandService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.CompanyRepository;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.DashboardRepository;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.WidgetRepository;

import java.util.Optional;

/**
 * Service implementation for handling Dashboard commands.
 */
@Service
@Transactional
public class DashboardCommandServiceImpl implements DashboardCommandService {
    private final DashboardRepository dashboardRepository;
    private final CompanyRepository companyRepository;
    private final WidgetRepository widgetRepository;

    /**
     * Constructor for DashboardCommandServiceImpl.
     *
     * @param dashboardRepository the repository for Dashboard persistence
     */
    public DashboardCommandServiceImpl(DashboardRepository dashboardRepository,
                                       CompanyRepository companyRepository,
                                       WidgetRepository widgetRepository) {
        this.dashboardRepository = dashboardRepository;
        this.companyRepository = companyRepository;
        this.widgetRepository = widgetRepository;
    }

    /**
     * Handles the creation of a new Dashboard.
     *
     * @param command the command to create a Dashboard
     * @return the generated ID of the new Dashboard
     */
    @Override
    public Long handle(CreateDashboardCommand command) {

        if (!companyRepository.existsById(command.companyId())) {
            throw new RuntimeException("Company with ID " + command.companyId() + " does not exist.");
        }
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
     *
     * @param command the command to update a Dashboard
     * @return the updated Dashboard as an Optional
     */
    @Override
    public Optional<Dashboard> handle(UpdateDashboardCommand command) {
        var dashboardId = command.dashboardId();
        if (!this.dashboardRepository.existsById(dashboardId)) {
            throw new RuntimeException("Dashboard with ID " + dashboardId + " does not exist.");
        }
        if (!this.companyRepository.existsById(command.companyId())) {
            throw new RuntimeException("Company with ID " + command.companyId() + " does not exist.");
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
     *
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

    /**
     * Handles the addition of a Widget to a Dashboard.
     *
     * @param command the command to add a Widget to a Dashboard
     */
    @Override
    public void handle(AddWidgetToDashboardCommand command) {
        var widget = widgetRepository.findById(command.widgetId())
                .orElseThrow(() -> new RuntimeException(
                        "Widget with ID " + command.widgetId() + " does not exist."));

        var dashboard = dashboardRepository.findById(command.dashboardId())
                .orElseThrow(() -> new RuntimeException(
                        "Dashboard with ID " + command.dashboardId() + " does not exist."));

        try {
            dashboard.addWidget(widget);
            dashboardRepository.save(dashboard);
        } catch (IllegalStateException ex) {
            throw new IllegalArgumentException("Domain error while adding Widget: " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error adding Widget to Dashboard: " + ex.getMessage(), ex);
        }
    }
}