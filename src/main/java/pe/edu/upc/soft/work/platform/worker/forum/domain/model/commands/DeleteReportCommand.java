package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

/**
 * Command to delete a Report
 * @param reportId the report id to delete
 */
public record DeleteReportCommand(Long reportId) {

    /**
     * Constructor with validation
     */
    public DeleteReportCommand{
        if (reportId == null || reportId <=0){
            throw new IllegalArgumentException("[DeleteReportCommand] reportId must be a positive number");
        }
    }
}
