package pe.edu.upc.soft.work.platform.worker.forum.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.AreaCompanyId;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;

/**
 * ReportCreatedEvent
 * Event triggered when a new Report is successfully created in the forum.
 * Mirrors the existing {@code MessagePostedEvent} pattern used elsewhere in this
 * bounded context, so downstream handlers (e.g. notifications) can react to it.
 */
@Getter
public class ReportCreatedEvent extends ApplicationEvent {

    /** The ID of the newly created report. */
    private final Long reportId;
    /** The user account ID of the reporter (who filed the report). */
    private final UserAccountId userAccountId;
    /** The area company the report was filed against/within. */
    private final AreaCompanyId areaCompanyId;

    /**
     * ReportCreatedEvent Constructor
     * @param source        the source of the event
     * @param reportId      the ID of the created report
     * @param userAccountId the reporter's user account ID
     * @param areaCompanyId the area company ID related to the report
     */
    public ReportCreatedEvent(Object source, Long reportId, UserAccountId userAccountId, AreaCompanyId areaCompanyId) {
        super(source);
        this.reportId = reportId;
        this.userAccountId = userAccountId;
        this.areaCompanyId = areaCompanyId;
    }
}
