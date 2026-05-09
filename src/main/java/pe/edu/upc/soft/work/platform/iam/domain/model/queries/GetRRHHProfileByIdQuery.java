package pe.edu.upc.soft.work.platform.iam.domain.model.queries;

/**
 * Query to retrieve an RRHH profile by its identifier.
 * @param RRHHProfileId the identifier of the RRHH profile to be retrieved
 */
public record GetRRHHProfileByIdQuery(Long RRHHProfileId) {

    /**
     * Constructor with validations.
     * @param RRHHProfileId the identifier of the RRHH profile to be retrieved
     */
    public GetRRHHProfileByIdQuery {
        if (RRHHProfileId == null) {
            throw new IllegalArgumentException("[GetRRHHProfileByIdQuery] RRHH profile id must not be null");
        }
    }
}
