package pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands;

/**
 * Command to delete a Performance
 */
public record DeletePerformanceCommand(Long performanceId) {

    /**
     * Constructor with validation
     */
    public DeletePerformanceCommand {
        if (performanceId == null || performanceId <= 0) {
            throw new IllegalArgumentException("[DeletePerformanceCommand] performanceId must be a positive number");
        }
    }
}
