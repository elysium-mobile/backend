package pe.edu.upc.soft.work.platform.notification.domain.model.aggregates;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.UpdateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects.NotificationType;
import pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects.UserAccountId;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Table(name="notifications")
public class Notification extends AuditableAbstractAggregateRoot<Notification> {

    @Getter
    @Column(name="seen", nullable = false)
    private boolean seen;

    @Getter
    private NotificationType notificationType;

    @Getter
    private Long userAccountId;

    /**
     * Default constructor for JPA
     */
    public Notification(){}

    /**
     * Constructor to create a Notification from CreateNotificationCommand
     * @param command the command containing notification
     */
    public Notification(CreateNotificationCommand command) {
        this.seen = false;
        this.notificationType = command.notificationType();
        this.userAccountId =command.userAccountId() ;
    }

    /**
     * Updates the Notification with details form an UpdateNotificationCommand
     * @param command the command containing updated notification detail
     */
    public void updateNotification(UpdateNotificationCommand command){
        this.seen = command.seen();
        this.notificationType = command.notificationType();
        this.userAccountId =command.userAccountId() ;
    }
}
