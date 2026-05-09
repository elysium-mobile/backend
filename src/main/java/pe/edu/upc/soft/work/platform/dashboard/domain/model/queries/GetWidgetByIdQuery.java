package pe.edu.upc.soft.work.platform.dashboard.domain.model.queries;

/**
 * Query to retrieve a Widget by their unique identifier.
 */
public record GetWidgetByIdQuery(Long widgetId) {

    /**
     * Constructor to validate the widgetId parameter.
     */
    public GetWidgetByIdQuery {
        if (widgetId == null || widgetId <= 0) {
            throw new IllegalArgumentException("Widget ID must be a positive number.");
        }
    }
}
