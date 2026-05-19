package pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries;

/**
 * Query to retrieve a Category by their unique identifier.
 */
public record GetCategoryByIdQuery(Long categoryId) {

    /**
     * Constructor to validate the categoryId parameter.
     */
    public GetCategoryByIdQuery {
        if (categoryId == null || categoryId <= 0) {
            throw new IllegalArgumentException("Category ID must be a positive number.");
        }
    }
}
