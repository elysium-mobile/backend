package pe.edu.upc.soft.work.platform.notification.domain.services;

import pe.edu.upc.soft.work.platform.notification.domain.model.aggregates.Notification;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.DeleteNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.UpdateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.entities.NotificationDetail;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetAllNotificationDetailQuery;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetAllNotificationsQuery;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationDetailByIdQuery;

import java.util.List;
import java.util.Optional;


public interface NotificationDetailQueryService {

    List<NotificationDetail> handle(GetAllNotificationDetailQuery query);

    Optional<NotificationDetail> handle(GetNotificationDetailByIdQuery query);

}
