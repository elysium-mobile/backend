package pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;

/**
 * Repository interface for managing Thread entities.
 */
@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long> {
}
