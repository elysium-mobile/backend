package pe.edu.upc.soft.work.platform.worker.forum.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.interfaces.acl.IamContextFacade;

/**
 * Outbound service to interact with the IAM context from the Worker Forum context.
 * Used to verify user account existence before associating messages or threads to users.
 */
@Service
public class ExternalIamServiceFromWorkerForum {

    /**
     * IAM context facade.
     */
    private final IamContextFacade iamContextFacade;

    /**
     * Constructor for ExternalIamServiceFromWorkerForum.
     *
     * @param iamContextFacade the IAM context facade
     */
    public ExternalIamServiceFromWorkerForum(IamContextFacade iamContextFacade) {
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
}
