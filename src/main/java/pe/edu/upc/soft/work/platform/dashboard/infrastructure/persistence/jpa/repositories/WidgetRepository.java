package pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.Widget;

/**
 * Repository interface for managing Widget entities.
 */
@Repository
public interface WidgetRepository extends JpaRepository<Widget, Long> {
}
