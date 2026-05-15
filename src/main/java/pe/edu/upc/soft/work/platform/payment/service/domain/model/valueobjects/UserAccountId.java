package pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value Object representing the identifier of a UserAccount
 * @param userAccountId the identifier of the Membership
 */
@Embeddable
public record UserAccountId(Long userAccountId) {

    public UserAccountId{
        if(userAccountId == null){
            throw new IllegalArgumentException("[userAccountId] must no be null");
        }
    }
}
