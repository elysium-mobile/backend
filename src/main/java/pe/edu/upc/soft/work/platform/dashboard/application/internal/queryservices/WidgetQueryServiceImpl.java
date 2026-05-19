package pe.edu.upc.soft.work.platform.dashboard.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.Widget;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetWidgetByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllWidgetQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.WidgetQueryService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.WidgetRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the WidgetQueryService interface.
 */
@Service
public class WidgetQueryServiceImpl implements WidgetQueryService {
    private final WidgetRepository widgetRepository;

    /**
     * Constructor for WidgetQueryServiceImpl.
     */
    public WidgetQueryServiceImpl(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    /**
     * Handles the GetAllWidgetQuery.
     */
    @Override
    public List<Widget> handle(GetAllWidgetQuery query) {
        return widgetRepository.findAll();
    }

    /**
     * Handles the GetWidgetByIdQuery.
     */
    @Override
    public Optional<Widget> handle(GetWidgetByIdQuery query) {
        return widgetRepository.findById(query.widgetId());
    }
}
