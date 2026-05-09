package pe.edu.upc.soft.work.platform.profile.performance.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.Performance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.DeletePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.services.PerformanceCommandService;
import pe.edu.upc.soft.work.platform.profile.performance.infrastructure.persistence.jpa.repositories.PerformanceRepository;

import java.util.Optional;

@Service
public class PerformanceCommandServiceImpl implements PerformanceCommandService {
    private final PerformanceRepository performanceRepository;

    public PerformanceCommandServiceImpl(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    @Override
    public Long handle(CreatePerformanceCommand command) {
        var performance = new Performance(command);
        try {
            performanceRepository.save(performance);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Performance: " + e.getMessage(), e);
        }
        return performance.getId();
    }

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
