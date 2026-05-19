package pe.edu.upc.soft.work.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.util.Objects;

/**
 * Value Object representing the identifier of a User Account.
 * @param userAccountId the unique identifier for the User Account
 */
@Embeddable
public record UserAccountId(Long userAccountId) {
    public UserAccountId {
        if (Objects.isNull(userAccountId) || userAccountId <= 0 ) {
            throw new IllegalArgumentException("[userAccountId] user account id cannot be null or negative");
        }
    }

    /**
     * Default constructor for JPA
     */
    public UserAccountId(){this(0L);}

    /**
     * Returns the user account ID value.
     *
     * @return the user account ID
     */
    public Long value() {
        return userAccountId;
    }
}
