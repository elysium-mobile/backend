package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Response object representing a Company in the system.
 */
public record CompanyResponse(
        @JsonProperty("comapany_id")Long companyId,
        String name,
        String RUC,
        String contactEmail,
        String contactPhone
) {}
