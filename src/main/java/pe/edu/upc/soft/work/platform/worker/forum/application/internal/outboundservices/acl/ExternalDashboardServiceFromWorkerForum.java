package pe.edu.upc.soft.work.platform.worker.forum.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.acl.DashboardContextFacade;

import java.util.Optional;

/**
 * Outbound service to interact with the Dashboard context from the Worker Forum context.
 * Used to verify company existence before associating a forum to a company.
 */
@Service
public class ExternalDashboardServiceFromWorkerForum {

    /**
     * Dashboard context facade.
     */
    private final DashboardContextFacade dashboardContextFacade;

    /**
     * Constructor for ExternalDashboardServiceFromWorkerForum.
     *
     * @param dashboardContextFacade the dashboard context facade
     */
    public ExternalDashboardServiceFromWorkerForum(DashboardContextFacade dashboardContextFacade) {
        this.dashboardContextFacade = dashboardContextFacade;
    }

    /**
     * Check if a company exists by its ID.
     *
     * @param companyId the ID of the company to verify
     * @return true if the company exists, false otherwise
     */
    public boolean existsCompanyById(Long companyId) {
        return this.dashboardContextFacade.existsCompanyById(companyId);
    }

    /**
     * Retrieves the name of a company by its ID.
     * Used by the Employee Assistant to enrich AI prompts with basic company context.
     *
     * @param companyId the ID of the company
     * @return an Optional containing the company name, or empty if not found
     */
    public Optional<String> getCompanyNameById(Long companyId) {
        return this.dashboardContextFacade.getCompanyNameById(companyId);
    }

}
