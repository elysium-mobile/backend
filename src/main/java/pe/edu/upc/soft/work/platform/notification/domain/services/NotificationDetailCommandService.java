package pe.edu.upc.soft.work.platform.notification.domain.services;

import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.DeleteNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.UpdateNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.entities.NotificationDetail;

import java.util.Optional;

public interface NotificationDetailCommandService {

    Long handle(CreateNotificationDetailCommand command);

    Optional<NotificationDetail> handle(UpdateNotificationDetailCommand command);

    void handle(DeleteNotificationDetailCommand command);
}
