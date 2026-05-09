package pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value Object representing the identifier of a WorkOfTeam entity.
 * @param workOfTeamId the unique identifier of the WorkOfTeam, must not be null.
 */
@Embeddable
public record WorkOfTeamId(Long workOfTeamId) {

    /**
     * Constructor for WorkOfTeamId that validates the input.
     * @param workOfTeamId the unique identifier of the WorkOfTeam, must not be null.
     */
    public WorkOfTeamId {
        if (workOfTeamId == null) {
            throw new IllegalArgumentException("WorkOfTeamId cannot be null");
        }
    }
}
