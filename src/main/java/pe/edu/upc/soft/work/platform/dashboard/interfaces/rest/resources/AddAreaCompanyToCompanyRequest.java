package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for adding an area company to a company.
 * @param areaCompanyId the ID of the area company to be added to the company
 */
public record AddAreaCompanyToCompanyRequest(
        @NotNull
        @JsonProperty("areaCompanyId")
        Long areaCompanyId
) {
}
