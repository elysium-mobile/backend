package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.Widget;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetWidgetByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllWidgetQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Widgets in the system.
 */
public interface WidgetQueryService {

    /**
     * Retrieves a list of all Widgets in the system.
     */
    List<Widget> handle(GetAllWidgetQuery query);

    /**
     * Retrieves a Widget by their unique identifier.
     */
    Optional<Widget> handle(GetWidgetByIdQuery query);
}
