package pe.edu.upc.soft.work.platform.notification.application.internal.commandservices;


import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.notification.application.internal.queryservices.NotificationDetailQueryServiceImpl;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.DeleteNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.UpdateNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.entities.NotificationDetail;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationDetailCommandService;
import pe.edu.upc.soft.work.platform.notification.infrastructure.persistence.jpa.repositories.NotificationDetailRepository;

import java.util.Optional;

@Service
public class NotificationDetailCommandServiceImpl implements NotificationDetailCommandService {

    private final NotificationDetailRepository notificationDetailRepository;

    public NotificationDetailCommandServiceImpl (NotificationDetailRepository notificationDetailRepository){
        this.notificationDetailRepository = notificationDetailRepository;
    }

    @Override
    public Long handle(CreateNotificationDetailCommand command) {
        var notificationDetail = new NotificationDetail(command);
        try {
            notificationDetailRepository.save(notificationDetail);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving notification detail: %s".formatted(e.getMessage()));
        }
        return notificationDetail.getId();
    }

    @Override
    public Optional<NotificationDetail> handle(UpdateNotificationDetailCommand command) {
        var notificationDetailId = command.notificationDetailId();
        var notificationDetailToUpdate = this.notificationDetailRepository.findById(notificationDetailId).get();
        notificationDetailToUpdate.updateNotificationDetail(command);
        try {
            var updatedNotificationDetail = notificationDetailRepository.save(notificationDetailToUpdate);
            return Optional.of(updatedNotificationDetail);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating notification detail: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(DeleteNotificationDetailCommand command) {
        if (!notificationDetailRepository.existsById(command.notificationDetailId()))
            throw new IllegalArgumentException("Notification detail with id %s not found".formatted(command.notificationDetailId()));
        try {
            notificationDetailRepository.deleteById(command.notificationDetailId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting notification detail: %s".formatted(e.getMessage()));
        }
    }
}
