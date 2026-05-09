package pe.edu.upc.soft.work.platform.dashboard.domain.model.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateWidgetCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateWidgetCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


/**
 * Widget aggregate root entity.
 */
@Entity
public class Widget extends AuditableAbstractAggregateRoot<Widget> {

    @Getter
    private String title;
    @Getter
    private Integer refreshPeriod;

    /**
     * Default constructor for JPA.
     */
    public Widget() {}

    /**
     * Constructor to create a Widget from a CreateWidgetCommand.
     * @param command the command containing widget details
     */
    public Widget(CreateWidgetCommand command) {
        this.title = command.title();
        this.refreshPeriod = command.refreshPeriod();
    }

    /**
     * Updates the Widget with details from an UpdateWidgetCommand.
     * @param command the command containing updated widget details
     */
    public void updateWidget(UpdateWidgetCommand command) {
        this.title = command.title();
        this.refreshPeriod = command.refreshPeriod();
    }
}
