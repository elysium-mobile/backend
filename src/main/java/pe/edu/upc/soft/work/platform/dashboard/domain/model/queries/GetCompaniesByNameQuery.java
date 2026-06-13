package pe.edu.upc.soft.work.platform.dashboard.domain.model.queries;

/**
 *  Query to retrieve companies by name.
 * @param name  the name of the company to search for.
 */
public record GetCompaniesByNameQuery(String name) {
}
