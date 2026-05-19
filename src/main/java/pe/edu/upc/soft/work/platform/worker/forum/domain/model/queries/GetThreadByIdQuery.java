package pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries;

/**
 * Query to retrieve a Thread by their unique identifier.
 */
public record GetThreadByIdQuery(Long threadId) {

    /**
     * Constructor to validate the threadId parameter.
     */
    public GetThreadByIdQuery {
        if (threadId == null || threadId <= 0) {
            throw new IllegalArgumentException("Thread ID must be a positive number.");
        }
    }
}
