package pe.edu.upc.soft.work.platform.profile.performance.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.CommentEmployee;

/**
 * Repository interface for managing CommentEmployee entities.
 */
@Repository
public interface CommentEmployeeRepository extends JpaRepository<CommentEmployee, Long> {
}
