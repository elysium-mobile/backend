package pe.edu.upc.soft.work.platform.notification.application.internal.commandservices;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.notification.application.internal.queryservices.NotificationDetailQueryServiceImpl;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.DeleteNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.UpdateNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.entities.NotificationDetail;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationDetailCommandService;
import pe.edu.upc.soft.work.platform.notification.infrastructure.persistence.jpa.repositories.NotificationDetailRepository;
import pe.edu.upc.soft.work.platform.notification.infrastructure.persistence.jpa.repositories.NotificationRepository;

import java.util.Optional;

@Service
@Transactional
public class NotificationDetailCommandServiceImpl implements NotificationDetailCommandService {

    private final NotificationDetailRepository notificationDetailRepository;
    private final NotificationRepository notificationRepository;

    public NotificationDetailCommandServiceImpl (NotificationDetailRepository notificationDetailRepository,
                                                 NotificationRepository notificationRepository){
        this.notificationDetailRepository = notificationDetailRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Long handle(CreateNotificationDetailCommand command) {
        if (!notificationRepository.existsById(command.notificationId()))
            throw new IllegalArgumentException("Notification with id %s not found".formatted(command.notificationId()));
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
        var notificationDetailToUpdate = this.notificationDetailRepository.findById(notificationDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Notification detail with id %s not found".formatted(notificationDetailId)));
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
