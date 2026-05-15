package pe.edu.upc.soft.work.platform.notification.domain.services;

import pe.edu.upc.soft.work.platform.notification.domain.model.aggregates.Notification;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.DeleteNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.UpdateNotificationCommand;

import java.util.Optional;

public interface NotificationCommandService {

    Long handle(CreateNotificationCommand command);

    Optional<Notification> handle(UpdateNotificationCommand command);

    void handle(DeleteNotificationCommand command);
}
