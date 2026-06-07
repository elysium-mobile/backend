package pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries;

/**
 * Query to retrieve a Report by their unique identifier.
 * @param reportId
 */
public record GetReportByIdQuery(Long reportId) {

    public GetReportByIdQuery{
        if (reportId == null || reportId <= 0) {
            throw new IllegalArgumentException("Report ID must be a positive number.");
        }
    }
}
