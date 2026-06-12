package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.application.internal.outboundservices.acl.ExternalIamServiceFromWorkerForum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Report;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateReportCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteReportCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateReportCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ReportCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ReportRepository;

import java.util.Optional;

/**
 * Service implementation for handling Report commands
 */
@Service
public class ReportCommandServiceImpl implements ReportCommandService {
    private final ReportRepository reportRepository;
    private final ExternalIamServiceFromWorkerForum externalIamServiceFromWorkerForum;

    /**
     * Constructor for ReportCommandServiceImpl
     * @param reportRepository the repository for Report persistence
     * @param externalIamServiceFromWorkerForum the external service for validating user accounts when creating or updating reports
     */
    public ReportCommandServiceImpl(ReportRepository reportRepository, ExternalIamServiceFromWorkerForum externalIamServiceFromWorkerForum) {
        this.reportRepository = reportRepository;
        this.externalIamServiceFromWorkerForum = externalIamServiceFromWorkerForum;
    }

    /**
     * Handles the creation of a Report
     * @param command the command to create a Report
     * @return the generated ID of the new Report
     */
    @Override
    public Long handle(CreateReportCommand command) {
        if (!this.externalIamServiceFromWorkerForum.existsUserAccountById(command.userAccountId().userAccountId())){
            throw new RuntimeException(String.format("[ReportCommandServiceImpl] Reporter ID: %s not found in the external IAM service",
                    command.userAccountId().userAccountId()));
        }
        var report = new Report(command);
        try{
            reportRepository.save(report);
        }catch (Exception e) {
            throw new RuntimeException("Error creating Report: " + e.getMessage(), e);
        }
        return report.getId();
    }


    /**
     * Handles the update of an existing Report
     * @param command the command to request the update of a Report
     * @return the updated Report as an Optional
     */
    @Override
    public Optional<Report> handle(UpdateReportCommand command) {
        var reportId = command.reportId();
        if (!this.reportRepository.existsById(reportId)){
            throw new RuntimeException(String.format("[ReportCommandServiceImpl] Report ID: %s not found in the repository",
                    reportId));
        }
        var reportToUpdate = this.reportRepository.findById(reportId).get();
        reportToUpdate.updateReport(command);
        try{
            var updatedReport = this.reportRepository.save(reportToUpdate);
            return Optional.of(updatedReport);
        }catch (Exception e) {
            throw new RuntimeException("Error updating Report: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of an existing Report
     * @param command the command to delete a Report
     */
    @Override
    public void handle(DeleteReportCommand command) {
        if (!reportRepository.existsById(command.reportId())){
            throw new RuntimeException(String.format("[ReportCommandServiceImpl] Report ID: %s not found in the repository",
                    command.reportId()));
        }
        try{
            reportRepository.deleteById(command.reportId());
        }catch (Exception e) {
            throw new RuntimeException("Error deleting Report: " + e.getMessage(), e);
        }
    }
}
