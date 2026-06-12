package pe.edu.upc.soft.work.platform.iam.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * UserRegisteredEvent
 * Event triggered when a new User is successfully registered.
 */
@Getter
public class UserRegisteredEvent extends ApplicationEvent {
    /** The ID of the registered User. */
    private final Long userId;

    /**
     * UserRegisteredEvent Constructor
     * @param source  the source of the event
     * @param userId  the ID of the newly registered user
     */
    public UserRegisteredEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }
}
