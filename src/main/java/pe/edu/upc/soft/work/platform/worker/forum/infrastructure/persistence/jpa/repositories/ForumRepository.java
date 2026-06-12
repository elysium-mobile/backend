package pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;

import java.awt.*;
import java.util.List;

/**
 * Repository interface for managing Forum entities.
 */
@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {

  List<Forum> findByCompanyId(Long companyId);
}
