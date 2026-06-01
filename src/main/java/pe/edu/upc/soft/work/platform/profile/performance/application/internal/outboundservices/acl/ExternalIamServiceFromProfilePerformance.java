package pe.edu.upc.soft.work.platform.profile.performance.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.interfaces.acl.IamContextFacade;

/**
 * Outbound service to interact with the IAM context from the Profile Performance context.
 * Used to verify that the employee profile referenced in a performance record actually exists.
 */
@Service
public class ExternalIamServiceFromProfilePerformance {

    /**
     * IAM context facade.
     */
    private final IamContextFacade iamContextFacade;

    /**
     * Constructor for ExternalIamServiceFromProfilePerformance.
     *
     * @param iamContextFacade the IAM context facade
     */
    public ExternalIamServiceFromProfilePerformance(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
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

    public boolean existsRRHHProfileById(Long RRHHProfileId){
        return this.iamContextFacade.existsRRHHProfileById(RRHHProfileId);
    }
}
