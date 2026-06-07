package pe.edu.upc.soft.work.platform.iam.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * UserAccountCreatedEvent
 * Event triggered when a new UserAccount is successfully created.
 */
@Getter
public class UserAccountCreatedEvent extends ApplicationEvent {
    /** The ID of the created UserAccount. */
    private final Long userAccountId;
    /** The userId associated with the account. */
    private final Long userId;
    /** The companyId associated with the account. */
    private final Long companyId;

    /**
     * UserAccountCreatedEvent Constructor
     * @param source        the source of the event
     * @param userAccountId the ID of the created user account
     * @param userId        the ID of the associated user
     * @param companyId     the ID of the associated company
     */
    public UserAccountCreatedEvent(Object source, Long userAccountId, Long userId, Long companyId) {
        super(source);
        this.userAccountId = userAccountId;
        this.userId = userId;
        this.companyId = companyId;
    }
}
