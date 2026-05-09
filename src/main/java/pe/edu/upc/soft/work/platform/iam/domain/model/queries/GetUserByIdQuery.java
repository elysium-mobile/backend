package pe.edu.upc.soft.work.platform.iam.domain.model.queries;

/**
 * Query to retrieve a user by their unique identifier.
 * @param userId the identifier of the user to be retrieved
 */
public record GetUserByIdQuery(Long userId) {

    /**
     * Constructor to validate the userId parameter.
     * @param userId the identifier of the user to be retrieved
     */
    public GetUserByIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number.");
        }
    }

}
