package pe.edu.upc.soft.work.platform.notification.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.notification.domain.model.entities.NotificationDetail;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetAllNotificationDetailQuery;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetAllNotificationsQuery;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationDetailByIdQuery;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationDetailQueryService;
import pe.edu.upc.soft.work.platform.notification.infrastructure.persistence.jpa.repositories.NotificationDetailRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationDetailQueryServiceImpl implements NotificationDetailQueryService {


    private final NotificationDetailRepository notificationDetailRepository;

    public NotificationDetailQueryServiceImpl(NotificationDetailRepository notificationDetailRepository) {
        this.notificationDetailRepository = notificationDetailRepository;
    }

    @Override
    public List<NotificationDetail> handle(GetAllNotificationDetailQuery query) {
        return notificationDetailRepository.findAll();
    }

    @Override
    public Optional<NotificationDetail> handle(GetNotificationDetailByIdQuery query) {
        return notificationDetailRepository.findById(query.notificationDetailId());
    }
}
