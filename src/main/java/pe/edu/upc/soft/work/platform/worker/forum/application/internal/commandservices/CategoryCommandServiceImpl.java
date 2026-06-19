package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.AddThreadToCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Category;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.CategoryCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.CategoryRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ForumRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ThreadRepository;

import java.util.Optional;

/**
 * Services implementation for handling Category commands
 */
@Service
public class CategoryCommandServiceImpl implements CategoryCommandService {
    private final CategoryRepository categoryRepository;
    private final ForumRepository forumRepository;
    private final ThreadRepository threadRepository;

    /**
     * Constructor for CategoryCommandServiceImpl.
     * @param categoryRepository the repository for Category persistence
     */
    public CategoryCommandServiceImpl(CategoryRepository categoryRepository,
                                      ForumRepository forumRepository,
                                      ThreadRepository threadRepository) {
        this.categoryRepository = categoryRepository;
        this.forumRepository = forumRepository;
        this.threadRepository= threadRepository;
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

        var forum = forumRepository.findById(command.forumId()).orElseThrow(()-> new RuntimeException("Forum with ID" + command.forumId()+"does not exists"));

        var category = new Category(command);
        try {
            categoryRepository.save(category);
            forum.addCategory(category);
            forumRepository.save(forum);
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
        var category = categoryRepository.findById(command.categoryId())
            .orElseThrow(() -> new RuntimeException("Category with ID " + command.categoryId() + " does not exist."));
        var forum = forumRepository.findById(category.getForumId())
            .orElseThrow(() -> new RuntimeException(
                "[CategoryCommandServiceImpl] Forum with ID " + category.getForumId() + " not found for Category " + command.categoryId()));
        try {
            forum.removeCategory(command.categoryId());
            forumRepository.save(forum);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Category: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(AddThreadToCategoryCommand command) {
        var thread = threadRepository.findById(command.threadId()).orElseThrow(() -> new RuntimeException(
                String.format("Thread with ID %s does not exist.", command.threadId())));
        var category = categoryRepository.findById(command.categoryId()).orElseThrow(() -> new RuntimeException(
                String.format("Category with ID %s does not exist.", command.categoryId())));
        try {
            category.addThread(thread);
            categoryRepository.save(category);
        } catch (Exception e) {
            throw new RuntimeException("Error adding Thread to Category: " + e.getMessage(), e);
        }
    }
}
