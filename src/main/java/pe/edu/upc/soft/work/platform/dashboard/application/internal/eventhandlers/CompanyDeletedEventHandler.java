package pe.edu.upc.soft.work.platform.dashboard.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.events.CompanyDeletedEvent;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.DashboardRepository;

/**
 * Event handler responsible for reacting to a CompanyDeletedEvent.
 * Removes all dashboards that were associated with the deleted company,
 * preventing orphaned records in the dashboards table.
 */
@Service
public class CompanyDeletedEventHandler {

    private final DashboardRepository dashboardRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDeletedEventHandler.class);

    /**
     * Constructor for CompanyDeletedEventHandler.
     * @param dashboardRepository repository for Dashboard persistence
     */
    public CompanyDeletedEventHandler(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    /**
     * Handles the CompanyDeletedEvent by deleting all dashboards linked to
     * the company that was just removed.
     * @param event the CompanyDeletedEvent containing the ID of the deleted company
     */
    @EventListener
    public void on(CompanyDeletedEvent event) {
        var orphanedDashboards = dashboardRepository.findByCompanyId(event.getCompanyId());
        if (orphanedDashboards.isEmpty()) {
            LOGGER.info("No dashboards found for deleted Company ID: {}", event.getCompanyId());
            return;
        }
        try {
            dashboardRepository.deleteAll(orphanedDashboards);
            LOGGER.info("Deleted {} dashboard(s) orphaned by removal of Company ID: {}",
                    orphanedDashboards.size(), event.getCompanyId());
        } catch (Exception e) {
            LOGGER.error("Error deleting orphaned dashboards for Company ID {}: {}",
                    event.getCompanyId(), e.getMessage());
        }
    }
}
