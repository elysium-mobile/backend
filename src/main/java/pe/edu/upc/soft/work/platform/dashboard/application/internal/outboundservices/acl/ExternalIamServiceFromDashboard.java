package pe.edu.upc.soft.work.platform.dashboard.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.interfaces.acl.IamContextFacade;

/**
 * Outbound service to interact with the IAM context from the Dashboard context.
 * Used to verify user account and employee profile existence when
 * managing companies, work teams, and units of work.
 */
@Service
public class ExternalIamServiceFromDashboard {

    /**
     * IAM context facade.
     */
    private final IamContextFacade iamContextFacade;

    /**
     * Constructor for ExternalIamServiceFromDashboard.
     *
     * @param iamContextFacade the IAM context facade
     */
    public ExternalIamServiceFromDashboard(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * Check if a user account exists by its ID.
     *
     * @param userAccountId the ID of the user account to verify
     * @return true if the user account exists, false otherwise
     */
    public boolean existsUserAccountById(Long userAccountId) {
        return this.iamContextFacade.existsUserAccountById(userAccountId);
    }

    /**
     * Check if an employee profile exists by its ID.
     *
     * @param employeeProfileId the ID of the employee profile to verify
     * @return true if the employee profile exists, false otherwise
     */
    public boolean existsEmployeeProfileById(Long employeeProfileId) {
        return this.iamContextFacade.existsEmployeeProfileById(employeeProfileId);
    }
}
