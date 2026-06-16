package pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.acl.DashboardContextFacade;

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

    public boolean existsWorkTeamById(Long workTeamId){
        return this.dashboardContextFacade.existsWorkTeamById(workTeamId);
    }

    public void addEmployeeToCompany(Long userAccountId, Long companyId) {
        this.dashboardContextFacade.addEmployeeToCompany(userAccountId, companyId);
    }
}
