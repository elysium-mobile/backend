package pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries;

/**
 * Query to retrieve a CommentEmployee by their unique identifier.
 */
public record GetCommentEmployeeByIdQuery(Long commentemployeeId) {

    /**
     * Constructor to validate the commentemployeeId parameter.
     */
    public GetCommentEmployeeByIdQuery {
        if (commentemployeeId == null || commentemployeeId <= 0) {
            throw new IllegalArgumentException("CommentEmployee ID must be a positive number.");
        }
    }
}
