package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import java.util.Date;

/**
 * Response object representing a Dashboard in the system.
 */
public record DashboardResponse(
        Long dashboardId,
        Long ruc
) {}
