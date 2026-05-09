package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

/**
 * Command to delete a Category
 */
public record DeleteCategoryCommand(Long categoryId) {

    /**
     * Constructor with validation
     */
    public DeleteCategoryCommand {
        if (categoryId == null || categoryId <= 0) {
            throw new IllegalArgumentException("[DeleteCategoryCommand] categoryId must be a positive number");
        }
    }
}
