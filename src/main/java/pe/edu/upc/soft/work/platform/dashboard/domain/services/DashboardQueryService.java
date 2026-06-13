package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Dashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetDashboardByCompanyIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetDashboardByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllDashboardQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Dashboards in the system.
 */
public interface DashboardQueryService {

    /**
     * Retrieves a list of all Dashboards in the system.
     */
    List<Dashboard> handle(GetAllDashboardQuery query);

    /**
     * Retrieves a Dashboard by their unique identifier.
     */
    Optional<Dashboard> handle(GetDashboardByIdQuery query);

    /**
     *  Retrieves a list of Dashboards that belong to a specific Company.
     * @param query the query containing the Company ID to search for
     * @return  a list of Dashboards that belong to the specified Company
     */
    List<Dashboard> handle(GetDashboardByCompanyIdQuery query);
}
