package pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries;

/**
 * Query to retrieve a Message by their unique identifier.
 */
public record GetMessageByIdQuery(Long messageId) {

    /**
     * Constructor to validate the messageId parameter.
     */
    public GetMessageByIdQuery {
        if (messageId == null || messageId <= 0) {
            throw new IllegalArgumentException("Message ID must be a positive number.");
        }
    }
}
