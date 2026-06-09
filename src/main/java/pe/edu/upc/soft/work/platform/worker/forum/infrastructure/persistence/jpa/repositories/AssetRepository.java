package pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Asset;

/**
 * Repository interface for managing Attachment entities.
 */
@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
}
