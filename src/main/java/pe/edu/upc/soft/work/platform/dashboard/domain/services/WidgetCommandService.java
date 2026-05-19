package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.Widget;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateWidgetCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateWidgetCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteWidgetCommand;

import java.util.Optional;

/**
 * Service interface for handling Widget-related commands.
 */
public interface WidgetCommandService {

    /**
     * Handles the creation of a new Widget.
     */
    Long handle(CreateWidgetCommand command);

    /**
     * Handles the update of an existing Widget.
     */
    Optional<Widget> handle(UpdateWidgetCommand command);

    /**
     * Handles the deletion of an existing Widget.
     */
    void handle(DeleteWidgetCommand command);
}
