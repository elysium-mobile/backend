package pe.edu.upc.soft.work.platform.dashboard.domain.model.queries;

/**
 * Query to retrieve a Company by their unique identifier.
 */
public record GetCompanyByIdQuery(Long companyId) {

    /**
     * Constructor to validate the companyId parameter.
     */
    public GetCompanyByIdQuery {
        if (companyId == null || companyId <= 0) {
            throw new IllegalArgumentException("Company ID must be a positive number.");
        }
    }
}
