package pe.edu.upc.soft.work.platform.feedback.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.interfaces.acl.IamContextFacade;

/**
 * Outbound service to interact with the IAM context from the Feedback context.
 * Used to verify user account existence when associating survey responses to users.
 */
@Service
public class ExternalIamServiceFromFeedback {

    /**
     * IAM context facade.
     */
    private final IamContextFacade iamContextFacade;

    /**
     * Constructor for ExternalIamServiceFromFeedback.
     *
     * @param iamContextFacade the IAM context facade
     */
    public ExternalIamServiceFromFeedback(IamContextFacade iamContextFacade) {
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


    public boolean existsEmployeeProfileById(Long employeeProfileId){
        return this.iamContextFacade.existsEmployeeProfileById(employeeProfileId);
    }
}
