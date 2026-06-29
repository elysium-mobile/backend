package pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.acl.DashboardContextFacade;

/**
 * Outbound service to interact with the Dashboard context from the IAM context.
 * Used to verify company and work team existence, and to manage employee
 * associations within companies.
 */
@Service
public class ExternalDashboardServiceFromIAM {


    /**
     * Dashboard context facade.
     */
    private final DashboardContextFacade dashboardContextFacade;

    /**
     * Constructor for ExternalDashboardServiceFromWorkerForum.
     *
     * @param dashboardContextFacade the dashboard context facade
     */
    public ExternalDashboardServiceFromIAM(DashboardContextFacade dashboardContextFacade) {
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
     * Check if a work team exists by its ID.
     *
     * @param workTeamId the ID of the work team to verify
     * @return true if the work team exists, false otherwise
     */
    public boolean existsWorkTeamById(Long workTeamId){
        return this.dashboardContextFacade.existsWorkTeamById(workTeamId);
    }

    /**
     * Associate an employee account with a company.
     *
     * @param userAccountId the ID of the user account to be added as an employee
     * @param companyId     the ID of the company
     */
    public void addEmployeeToCompany(Long userAccountId, Long companyId) {
        this.dashboardContextFacade.addEmployeeToCompany(userAccountId, companyId);
    }
}
