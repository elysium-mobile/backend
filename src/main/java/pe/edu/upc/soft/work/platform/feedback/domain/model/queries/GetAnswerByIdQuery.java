package pe.edu.upc.soft.work.platform.feedback.domain.model.queries;

/**
 * Query to retrieve a Answer by their unique identifier.
 */
public record GetAnswerByIdQuery(Long answerId) {

    /**
     * Constructor to validate the answerId parameter.
     */
    public GetAnswerByIdQuery {
        if (answerId == null || answerId <= 0) {
            throw new IllegalArgumentException("Answer ID must be a positive number.");
        }
    }
}
