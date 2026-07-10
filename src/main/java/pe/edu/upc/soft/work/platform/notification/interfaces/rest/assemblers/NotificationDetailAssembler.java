package pe.edu.upc.soft.work.platform.notification.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.UpdateNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.entities.NotificationDetail;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.CreateNotificationDetailRequest;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.NotificationDetailResponse;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.UpdateNotificationDetailRequest;

public class NotificationDetailAssembler {

    public static CreateNotificationDetailCommand toCommandFromRequest(CreateNotificationDetailRequest request){
        return new CreateNotificationDetailCommand(
                request.title(),
                request.content(),
                request.notificationId()
        );
    }

    public static UpdateNotificationDetailCommand toCommandFromRequest(Long notificationDetailId, UpdateNotificationDetailRequest request){
        return new UpdateNotificationDetailCommand(
                notificationDetailId,
                request.title(),
                request.content(),
                request.notificationId()
        );
    }

    public static NotificationDetailResponse toResponseFromEntity(NotificationDetail entity){
        return new NotificationDetailResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getNotificationId()
        );
    }
}
