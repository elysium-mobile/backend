package pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value Object representing the identifier of a UserAccount
 * @param userAccountId the identifier of the UserAccount
 */
@Embeddable
public record UserAccountId(Long userAccountId) {
    public UserAccountId{
        if (userAccountId == null){
            throw new IllegalArgumentException("[userAccountId] must not be null");
        }
    }
}
