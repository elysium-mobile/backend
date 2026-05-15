package pe.edu.upc.soft.work.platform.notification.domain.services;

import pe.edu.upc.soft.work.platform.notification.domain.model.aggregates.Notification;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetAllNotificationsQuery;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationByIdQuery;

import java.util.List;
import java.util.Optional;

public interface NotificationQueryService {


    List<Notification> handle(GetAllNotificationsQuery query);

    Optional<Notification> handle(GetNotificationByIdQuery query);
}
