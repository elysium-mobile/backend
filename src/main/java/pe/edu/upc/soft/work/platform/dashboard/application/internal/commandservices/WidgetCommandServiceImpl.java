package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.Widget;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateWidgetCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateWidgetCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteWidgetCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.WidgetCommandService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.WidgetRepository;

import java.util.Optional;

/**
 * Service implementation for handling Widget commands.
 */
@Service
public class WidgetCommandServiceImpl implements WidgetCommandService {
    private final WidgetRepository widgetRepository;

    /**
     * Constructor for WidgetCommandServiceImpl.
     * @param widgetRepository the repository for Widget persistence
     */
    public WidgetCommandServiceImpl(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    /**
     * Handles the creation of a new Widget.
     * @param command the command to create a Widget
     * @return the generated ID of the new Widget
     */
    @Override
    public Long handle(CreateWidgetCommand command) {
        var widget = new Widget(command);
        try {
            widgetRepository.save(widget);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Widget: " + e.getMessage(), e);
        }
        return widget.getId();
    }

    /**
     * Handles the update of an existing Widget.
     * @param command the command to update a Widget
     * @return the updated Widget as an Optional
     */
    @Override
    public Optional<Widget> handle(UpdateWidgetCommand command) {
        var widgetId = command.widgetId();
        if (!this.widgetRepository.existsById(widgetId)) {
            throw new RuntimeException("Widget with ID " + widgetId + " does not exist.");
        }

        var widgetToUpdate = this.widgetRepository.findById(widgetId).get();
        widgetToUpdate.updateWidget(command);
        try {
            var updatedWidget = this.widgetRepository.save(widgetToUpdate);
            return Optional.of(updatedWidget);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Widget: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of a Widget.
     * @param command the command to delete a Widget
     */
    @Override
    public void handle(DeleteWidgetCommand command) {
        if (!widgetRepository.existsById(command.widgetId())) {
            throw new RuntimeException("Widget with ID " + command.widgetId() + " does not exist.");
        }
        try {
            widgetRepository.deleteById(command.widgetId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Widget: " + e.getMessage(), e);
        }
    }
}