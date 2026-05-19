package pe.edu.upc.soft.work.platform.dashboard.domain.model.queries;

/**
 * Query to retrieve a AreaCompany by their unique identifier.
 */
public record GetAreaCompanyByIdQuery(Long areacompanyId) {

    /**
     * Constructor to validate the areacompanyId parameter.
     */
    public GetAreaCompanyByIdQuery {
        if (areacompanyId == null || areacompanyId <= 0) {
            throw new IllegalArgumentException("AreaCompany ID must be a positive number.");
        }
    }
}
