package pe.edu.upc.soft.work.platform.worker.forum.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Category;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetCategoryByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllCategoryQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.CategoryQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the CategoryQueryService interface.
 */
@Service
public class CategoryQueryServiceImpl implements CategoryQueryService {
    private final CategoryRepository categoryRepository;

    /**
     * Constructor for CategoryQueryServiceImpl.
     */
    public CategoryQueryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Handles the GetAllCategoryQuery.
     */
    @Override
    public List<Category> handle(GetAllCategoryQuery query) {
        return categoryRepository.findAll();
    }

    /**
     * Handles the GetCategoryByIdQuery.
     */
    @Override
    public Optional<Category> handle(GetCategoryByIdQuery query) {
        return categoryRepository.findById(query.categoryId());
    }
}
