package pe.edu.upc.soft.work.platform.worker.forum.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects.NotificationType;
import pe.edu.upc.soft.work.platform.notification.interfaces.acl.NotificationContextFacade;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.events.ReportCreatedEvent;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetReportByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ReportQueryService;

/**
 *  ReportCreatedEventHandler
 *  Event handler that listens for ReportCreatedEvent and triggers the creation of a notification
 *  in the Notification bounded context for the reporter.
 *  This handler is designed to be best-effort: if notification creation fails, it logs the error but does not roll back the report creation.
 */
@Service
public class ReportCreatedEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportCreatedEventHandler.class);

    private final ReportQueryService reportQueryService;
    private final NotificationContextFacade notificationContextFacade;

    /**
     * Constructor for ReportCreatedEventHandler.
     * @param reportQueryService         service to query the Report aggregate
     * @param notificationContextFacade  ACL facade used to create notifications in the Notification bounded context
     */
    public ReportCreatedEventHandler(ReportQueryService reportQueryService,
                                     NotificationContextFacade notificationContextFacade) {
        this.reportQueryService = reportQueryService;
        this.notificationContextFacade = notificationContextFacade;
    }

    /**
     * Handles the ReportCreatedEvent after a new report has been successfully created.
     * @param event the ReportCreatedEvent containing report, reporter and area details
     */
    @EventListener
    public void on(ReportCreatedEvent event) {
        var report = reportQueryService.handle(new GetReportByIdQuery(event.getReportId()));

        if (report.isEmpty()) {
            LOGGER.warn("Error: Report with ID {} could not be found after creation.", event.getReportId());
            return;
        }

        LOGGER.info("Report successfully created with ID: {} by UserAccount ID: {} in AreaCompany ID: {}",
                event.getReportId(), event.getUserAccountId().userAccountId(), event.getAreaCompanyId().areaCompanyId());

        // Notification creation is best-effort: a failure here (e.g. the reporter's
        // account can't be resolved by IAM) must never roll back or mask the fact
        // that the report itself was created successfully.
        try {
            notificationContextFacade.createNotification(NotificationType.FORUM, event.getUserAccountId().userAccountId());
        } catch (Exception e) {
            LOGGER.warn("Could not create confirmation notification for reporter UserAccount ID: {} on Report ID: {}. Reason: {}",
                    event.getUserAccountId().userAccountId(), event.getReportId(), e.getMessage());
        }
    }
}
