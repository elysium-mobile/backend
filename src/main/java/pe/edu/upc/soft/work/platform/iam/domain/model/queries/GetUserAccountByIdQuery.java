package pe.edu.upc.soft.work.platform.iam.domain.model.queries;

/**
 * Query to retrieve a user account by its identifier.
 * @param UserAccountId the identifier of the user account to be retrieved
 */
public record GetUserAccountByIdQuery(Long UserAccountId) {

    /**
     * Validates the input parameters for the query.
     * @param UserAccountId the identifier of the user account to be retrieved
     */
    public GetUserAccountByIdQuery {
        if (UserAccountId == null || UserAccountId <= 0) {
            throw new IllegalArgumentException("UserAccountId must be a positive number.");
        }
    }
}
