package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Report;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateReportCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteReportCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateReportCommand;

import java.util.Optional;

/**
 * Service interface for handling Report-related commands
 */
public interface ReportCommandService {

    /**
     * Handles the creation of a new Report.
     */
    Long handle(CreateReportCommand command);

    /**
     * Handles the update of an existing Report.
     */
    Optional<Report> handle(UpdateReportCommand command);

    /**
     * Handles the deletion of an existing Report.
     */
     void handle(DeleteReportCommand command);
}
