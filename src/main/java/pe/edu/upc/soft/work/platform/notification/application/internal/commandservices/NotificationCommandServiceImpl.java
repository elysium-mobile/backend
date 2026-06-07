package pe.edu.upc.soft.work.platform.notification.application.internal.commandservices;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.notification.application.internal.outboundservices.acl.ExternalIamServiceFromNotification;
import pe.edu.upc.soft.work.platform.notification.domain.model.aggregates.Notification;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.DeleteNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.UpdateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.events.NotificationCreatedEvent;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationCommandService;
import pe.edu.upc.soft.work.platform.notification.infrastructure.persistence.jpa.repositories.NotificationRepository;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

import java.util.Optional;

@Service
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationRepository notificationRepository;
    private final ExternalIamServiceFromNotification externalIamServiceFromNotification;
    private final ApplicationEventPublisher eventPublisher;

    public NotificationCommandServiceImpl(NotificationRepository notificationRepository,
                                          ExternalIamServiceFromNotification externalIamServiceFromNotification,
                                          ApplicationEventPublisher eventPublisher) {
        this.notificationRepository = notificationRepository;
        this.externalIamServiceFromNotification = externalIamServiceFromNotification;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Long handle(CreateNotificationCommand command) {
        if (!externalIamServiceFromNotification.existsUserAccountById(command.userAccountId())){
            throw new NotFoundArgumentException(
                    String.format("[NotificationCommandServiceImpl] User Account ID: %s not found in the external IAM service",
                            command.userAccountId()));
        }

        var notification = new Notification(command);
        eventPublisher.publishEvent(new NotificationCreatedEvent(this, notification.getId(), notification.getUserAccountId(), notification.getNotificationType()));
        try {
            notificationRepository.save(notification);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving notification: %s".formatted(e.getMessage()));
        }
        return notification.getId();
    }

    @Override
    public Optional<Notification> handle(UpdateNotificationCommand command) {
        var notificationId = command.notificationId();
        var notificationToUpdate = this.notificationRepository.findById(notificationId).get();
        notificationToUpdate.updateNotification(command);
        try {
            var updatedNotification = notificationRepository.save(notificationToUpdate);
            return Optional.of(updatedNotification);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating notification: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(DeleteNotificationCommand command) {

        if (!notificationRepository.existsById(command.notificationId()))
            throw new IllegalArgumentException("Notification with id %s not found".formatted(command.notificationId()));
        try {
            notificationRepository.deleteById(command.notificationId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting notification: %s".formatted(e.getMessage()));
        }
    }
}
