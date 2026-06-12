package pe.edu.upc.soft.work.platform.dashboard.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.events.CompanyCreatedEvent;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetCompanyByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.CompanyQueryService;

/**
 * Event handler responsible for reacting to a successful CompanyCreatedEvent.
 */
@Service
public class CompanyCreatedEventHandler {

    private final CompanyQueryService companyQueryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyCreatedEventHandler.class);

    /**
     * Constructor for CompanyCreatedEventHandler.
     * @param companyQueryService service to query the Company aggregate
     */
    public CompanyCreatedEventHandler(CompanyQueryService companyQueryService) {
        this.companyQueryService = companyQueryService;
    }

    /**
     * Handles the CompanyCreatedEvent after a new company has been successfully created.
     * @param event the CompanyCreatedEvent containing the ID and name of the created company
     */
    @EventListener
    public void on(CompanyCreatedEvent event) {
        var getCompanyByIdQuery = new GetCompanyByIdQuery(event.getCompanyId());
        var company = companyQueryService.handle(getCompanyByIdQuery);

        if (company.isPresent()) {
            LOGGER.info("Company successfully created with ID: {} and name: {}",
                    event.getCompanyId(), event.getCompanyName());
        } else {
            LOGGER.warn("Error: Company with ID {} could not be found after creation.", event.getCompanyId());
        }
    }
}
