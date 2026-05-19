package pe.edu.upc.soft.work.platform.profile.performance.domain.services;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.Performance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.DeletePerformanceCommand;

import java.util.Optional;

/**
 * Service interface for handling Performance-related commands.
 */
public interface PerformanceCommandService {

    /**
     * Handles the creation of a new Performance.
     */
    Long handle(CreatePerformanceCommand command);

    /**
     * Handles the update of an existing Performance.
     */
    Optional<Performance> handle(UpdatePerformanceCommand command);

    /**
     * Handles the deletion of an existing Performance.
     */
    void handle(DeletePerformanceCommand command);
}
