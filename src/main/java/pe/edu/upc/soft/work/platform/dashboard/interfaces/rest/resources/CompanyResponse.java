package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import java.util.Date;

/**
 * Response object representing a Company in the system.
 */
public record CompanyResponse(
        Long companyId,
        String name,
        String RUC,
        String contactEmail,
        String contactPhone
) {}
