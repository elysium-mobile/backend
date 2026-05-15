package pe.edu.upc.soft.work.platform.notification.domain.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jdk.jfr.Name;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.UpdateNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Table(name = "notificationDetail")
public class NotificationDetail extends AuditableAbstractAggregateRoot<NotificationDetail> {

    @Getter
    private String title;

    @Getter
    private String content;

    /**
     * Default constructor for JPA
     */
    public NotificationDetail() {

    }

    public NotificationDetail(CreateNotificationDetailCommand command){
        this.title = command.title();
        this.content = command.content();
    }

    public void updateNotificationDetail(UpdateNotificationDetailCommand command){
        this.title = command.title();
        this.content = command.content();
    }
}
