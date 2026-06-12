package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UserAccountResponse;

import java.util.Date;
import java.util.List;

/**
 * Response object representing a Company in the system.
 */
public record CompanyResponse(
        @JsonProperty("comapany_id")Long companyId,
        String name,
        String RUC,
        String contactEmail,
        String contactPhone,
        List<UserAccountResponse> employees,
        List<AreaCompanyResponse> areaCompanyResponses
) {}
