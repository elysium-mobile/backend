package pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries;

/**
 * Query to retrieve a Forum by their unique identifier.
 */
public record GetForumByIdQuery(Long forumId) {

    /**
     * Constructor to validate the forumId parameter.
     */
    public GetForumByIdQuery {
        if (forumId == null || forumId <= 0) {
            throw new IllegalArgumentException("Forum ID must be a positive number.");
        }
    }
}
