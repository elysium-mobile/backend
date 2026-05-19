package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Category;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetCategoryByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllCategoryQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Categorys in the system.
 */
public interface CategoryQueryService {

    /**
     * Retrieves a list of all Categorys in the system.
     */
    List<Category> handle(GetAllCategoryQuery query);

    /**
     * Retrieves a Category by their unique identifier.
     */
    Optional<Category> handle(GetCategoryByIdQuery query);
}
