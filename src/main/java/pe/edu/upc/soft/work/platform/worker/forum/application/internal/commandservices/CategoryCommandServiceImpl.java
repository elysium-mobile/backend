package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Category;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.CategoryCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.CategoryRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ForumRepository;

import java.util.Optional;

/**
 * Services implementation for handling Category commands
 */
@Service
public class CategoryCommandServiceImpl implements CategoryCommandService {
    private final CategoryRepository categoryRepository;
    private final ForumRepository forumRepository;

    /**
     * Constructor for CategoryCommandServiceImpl.
     * @param categoryRepository the repository for Category persistence
     */
    public CategoryCommandServiceImpl(CategoryRepository categoryRepository,
                                      ForumRepository forumRepository) {
        this.categoryRepository = categoryRepository;
        this.forumRepository = forumRepository;
    }

    /**
     * Handles the creation of an Category
     * @param command the command to create a Category
     * @return the generated ID of the new Category
     */
    @Override
    public Long handle(CreateCategoryCommand command) {
        if (!forumRepository.existsById(command.forumId())) {
            throw new RuntimeException("Forum with ID " + command.forumId() + " does not exist.");
        }
        var category = new Category(command);
        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Category: " + e.getMessage(), e);
        }
        return category.getId();
    }

    /**
     * Handles the update of an existing Category
     * @param command the command to update a Category
     * @return to updated Category as an Optional
     */
    @Override
    public Optional<Category> handle(UpdateCategoryCommand command) {

        if (!forumRepository.existsById(command.forumId())) {
            throw new RuntimeException("Forum with ID " + command.forumId() + " does not exist.");
        }
        var categoryId = command.categoryId();
        if (!this.categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("Category with ID " + categoryId + " does not exist.");
        }

        var categoryToUpdate = this.categoryRepository.findById(categoryId).get();
        categoryToUpdate.updateCategory(command);
        try {
            var updatedCategory = this.categoryRepository.save(categoryToUpdate);
            return Optional.of(updatedCategory);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Category: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of an Category
     * @param command the command to delete an Category
     */
    @Override
    public void handle(DeleteCategoryCommand command) {
        if (!categoryRepository.existsById(command.categoryId())) {
            throw new RuntimeException("Category with ID " + command.categoryId() + " does not exist.");
        }
        try {
            categoryRepository.deleteById(command.categoryId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Category: " + e.getMessage(), e);
        }
    }
}
