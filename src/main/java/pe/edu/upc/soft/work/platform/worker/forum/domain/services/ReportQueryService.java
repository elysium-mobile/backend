package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Report;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllReportsQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetReportByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Reports in the system. It defines methods for retrieving all reports and fetching a report by its unique identifier.
 */
public interface ReportQueryService {

    /**
     * Retrieves a list of all Reports in the system.
     */
    List<Report> handle(GetAllReportsQuery query);

    /**
     * Retrieves a Report by its unique identifier.
     */
    Optional<Report> handle(GetReportByIdQuery query);
}
