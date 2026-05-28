package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Category;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CategoryResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateCategoryRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateCategoryRequest;

public class CategoryAssembler {

    /**
     * Converts a CreateCategoryRequest to a CreateCategoryCommand.
     */
    public static CreateCategoryCommand toCommandFromRequest(CreateCategoryRequest request) {
        return new CreateCategoryCommand(request.title(), request.description());
    }

    /**
     * Converts an UpdateCategoryRequest to an UpdateCategoryCommand.
     */
    public static UpdateCategoryCommand toCommandFromRequest(Long categoryId, UpdateCategoryRequest request) {
        return new UpdateCategoryCommand(categoryId, request.title(), request.description());
    }

    /**
     * Converts a Category entity to a CategoryResponse.
     */
    public static CategoryResponse toResponseFromEntity(Category category) {
        return new CategoryResponse(category.getId(), category.getTitle(), category.getDescription());
    }
}
