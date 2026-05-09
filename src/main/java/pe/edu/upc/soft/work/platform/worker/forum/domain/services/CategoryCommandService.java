package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Category;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteCategoryCommand;

import java.util.Optional;

/**
 * Service interface for handling Category-related commands.
 */
public interface CategoryCommandService {

    /**
     * Handles the creation of a new Category.
     */
    Long handle(CreateCategoryCommand command);

    /**
     * Handles the update of an existing Category.
     */
    Optional<Category> handle(UpdateCategoryCommand command);

    /**
     * Handles the deletion of an existing Category.
     */
    void handle(DeleteCategoryCommand command);
}
