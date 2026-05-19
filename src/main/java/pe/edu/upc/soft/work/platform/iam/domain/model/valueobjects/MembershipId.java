package pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing the identifier of a membership.
 * @param MembershipId the identifier of the membership
 */
@Embeddable
public record MembershipId(Long MembershipId) {

    /**
     * Constructor with validation.
     * @param MembershipId the identifier of the membership
     */
    public MembershipId {
        if (MembershipId == null) {
            throw new IllegalArgumentException("[MembershipId] MembershipId must not be null");
        }
    }
}
