package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.util.Date;
import java.util.List;

/**
 * Response object representing a Dashboard in the system.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DashboardResponse(
        Long dashboardId,
        String title,
        String description,
        String ruc,
        Long companyId,
        List<WidgetResponse> widgets
) {}
