package pe.edu.upc.soft.work.platform.notification.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.notification.domain.model.aggregates.Notification;

/**
 * Repository interface for managing Notification entities
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
