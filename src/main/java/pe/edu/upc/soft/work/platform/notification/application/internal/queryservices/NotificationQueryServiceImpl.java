package pe.edu.upc.soft.work.platform.notification.application.internal.queryservices;


import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.notification.domain.model.aggregates.Notification;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetAllNotificationsQuery;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationByIdQuery;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationQueryService;
import pe.edu.upc.soft.work.platform.notification.infrastructure.persistence.jpa.repositories.NotificationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository notificationRepository;

    public NotificationQueryServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> handle(GetAllNotificationsQuery query) {
        return notificationRepository.findAll();
    }

    @Override
    public Optional<Notification> handle(GetNotificationByIdQuery query) {
        return notificationRepository.findById(query.notificationId());
    }
}
