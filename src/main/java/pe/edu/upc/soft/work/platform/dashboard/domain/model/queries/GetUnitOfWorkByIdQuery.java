package pe.edu.upc.soft.work.platform.dashboard.domain.model.queries;

/**
 * Query to retrieve a UnitOfWork by their unique identifier.
 */
public record GetUnitOfWorkByIdQuery(Long unitofworkId) {

    /**
     * Constructor to validate the unitofworkId parameter.
     */
    public GetUnitOfWorkByIdQuery {
        if (unitofworkId == null || unitofworkId <= 0) {
            throw new IllegalArgumentException("UnitOfWork ID must be a positive number.");
        }
    }
}
