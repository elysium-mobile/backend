package pe.edu.upc.soft.work.platform.notification.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.interfaces.acl.IamContextFacade;

/**
 * Outbound service to interact with the IAM context from the Notification context.
 * Used to verify user account existence before creating a notification for a target user.
 */
@Service
public class ExternalIamServiceFromNotification {

    /**
     * IAM context facade.
     */
    private final IamContextFacade iamContextFacade;

    /**
     * Constructor for ExternalIamServiceFromNotification.
     *
     * @param iamContextFacade the IAM context facade
     */
    public ExternalIamServiceFromNotification(IamContextFacade iamContextFacade) {
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
