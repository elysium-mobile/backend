package pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Category;

/**
 * Repository interface for managing Category entities.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
