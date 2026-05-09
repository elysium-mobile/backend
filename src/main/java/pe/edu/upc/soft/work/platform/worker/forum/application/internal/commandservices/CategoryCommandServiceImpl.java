package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Category;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.CategoryCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.CategoryRepository;

import java.util.Optional;

@Service
public class CategoryCommandServiceImpl implements CategoryCommandService {
    private final CategoryRepository categoryRepository;

    public CategoryCommandServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Long handle(CreateCategoryCommand command) {
        var category = new Category(command);
        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Category: " + e.getMessage(), e);
        }
        return category.getId();
    }

    @Override
    public Optional<Category> handle(UpdateCategoryCommand command) {
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
