package pe.edu.upc.soft.work.platform.notification.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.notification.domain.model.aggregates.Notification;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.UpdateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects.NotificationType;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.CreateNotificationRequest;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.NotificationResponse;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.UpdateNotificationRequest;

public class NotificationAssembler {


    public static CreateNotificationCommand toCommandFromRequest(CreateNotificationRequest request){
        return new CreateNotificationCommand(
                request.seen(),
                NotificationType.valueOf(request.notificationType()),
                request.userAccountId()
        );
    }

    public static UpdateNotificationCommand toCommandFromRequest(Long notificationId, UpdateNotificationRequest request){
        return new UpdateNotificationCommand(
                notificationId,
                request.seen(),
                NotificationType.valueOf(request.notificationType()),
                request.userAccountId()
        );
    }

    public static NotificationResponse toResponseFromEntity(Notification entity){
        return new NotificationResponse(
                entity.getId(),
                entity.isSeen(),
                entity.getNotificationType().name(),
                entity.getUserAccountId()
        );
    }
}
