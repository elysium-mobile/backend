package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Dashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.*;

import java.util.List;
import java.util.Map;
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


    /**
     * Returns the average performance classification (1-5) and total evaluations
     * for all employees of a company.
     * Useful for a gauge chart or KPI card in the mobile dashboard.
     */
    Map<String, Object> handle(GetAveragePerformanceByCompanyQuery query);

    /**
     * Returns the percentage of positive survey answers (scoreAnswer >= threshold)
     * for a given company.
     * Useful for a donut/pie chart showing positive vs negative sentiment.
     */
    Map<String, Object> handle(GetPositiveSurveyRateByCompanyQuery query);

    /**
     * Returns the number of reports filed per area within a company.
     * Useful for a bar chart comparing incident volume across departments.
     */
    Map<String, Object> handle(GetReportCountByCompanyQuery query);

    /**
     * Returns forum activity metrics (thread count + total messages) per area.
     * Useful for a grouped bar chart showing communication activity per department.
     */
    Map<String, Object> handle(GetForumActivityByCompanyQuery query);

    /**
     * Returns the number of employees per area within a company.
     * Useful for a horizontal bar or donut chart showing workforce distribution.
     */
    Map<String, Object> handle(GetEmployeeCountByAreaQuery query);
}
