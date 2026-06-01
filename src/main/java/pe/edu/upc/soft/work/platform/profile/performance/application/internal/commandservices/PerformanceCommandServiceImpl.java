package pe.edu.upc.soft.work.platform.profile.performance.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.application.internal.outboundservices.acl.ExternalIamServiceFromPaymentService;
import pe.edu.upc.soft.work.platform.profile.performance.application.internal.outboundservices.acl.ExternalIamServiceFromProfilePerformance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.Performance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.DeletePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.services.PerformanceCommandService;
import pe.edu.upc.soft.work.platform.profile.performance.infrastructure.persistence.jpa.repositories.PerformanceRepository;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

import java.util.Optional;

/**
 * Service implementation for handling Performance commands
 */
@Service
public class PerformanceCommandServiceImpl implements PerformanceCommandService {
    private final PerformanceRepository performanceRepository;
    private final ExternalIamServiceFromProfilePerformance externalIamServiceFromProfilePerformance;

    /**
     * Constructor for PerformanceCommandServiceImpl
     * @param performanceRepository the repository for Performance persistence
     */
    public PerformanceCommandServiceImpl(PerformanceRepository performanceRepository,
                                         ExternalIamServiceFromProfilePerformance externalIamServiceFromProfilePerformance) {
        this.performanceRepository = performanceRepository;
        this.externalIamServiceFromProfilePerformance = externalIamServiceFromProfilePerformance;
    }

    /**
     * Handles the creation of a Performance entity based on the provided command.
     * @param command the command to create a Performance
     * @return the generated ID of the new Performance
     */
    @Override
    public Long handle(CreatePerformanceCommand command) {
        if(!externalIamServiceFromProfilePerformance.existsEmployeeProfileById(command.employeeProfileId().employeeProfileId())){
            throw new NotFoundArgumentException(
                    String.format("[PerformanceCommandServiceImpl] Employee Profile ID: %s not found in the external IAM service",
                            command.employeeProfileId().employeeProfileId())
            );
        }
        var performance = new Performance(command);
        try {
            performanceRepository.save(performance);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Performance: " + e.getMessage(), e);
        }
        return performance.getId();
    }

    /**
     * Handles the update of an existing Performance
     * @param command the command to update a Performance
     * @return the updated Performance as an Optional
     */
    @Override
    public Optional<Performance> handle(UpdatePerformanceCommand command) {
        var performanceId = command.performanceId();
        if (!this.performanceRepository.existsById(performanceId)) {
            throw new RuntimeException("Performance with ID " + performanceId + " does not exist.");
        }

        var performanceToUpdate = this.performanceRepository.findById(performanceId).get();
        performanceToUpdate.updatePerformance(command);
        try {
            var updatedPerformance = this.performanceRepository.save(performanceToUpdate);
            return Optional.of(updatedPerformance);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Performance: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of an exiting Performance
     * @param command  the command to delete a Performance
     */
    @Override
    public void handle(DeletePerformanceCommand command) {
        if (!performanceRepository.existsById(command.performanceId())) {
            throw new RuntimeException("Performance with ID " + command.performanceId() + " does not exist.");
        }
        try {
            performanceRepository.deleteById(command.performanceId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Performance: " + e.getMessage(), e);
        }
    }
}
