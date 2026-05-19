package pe.edu.upc.soft.work.platform.notification.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.notification.domain.model.entities.NotificationDetail;


/**
 * Repository interface for managing Notification Detail entities
 */
@Repository
public interface NotificationDetailRepository extends JpaRepository<NotificationDetail, Long> {
}
