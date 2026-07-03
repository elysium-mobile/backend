package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UserAccountResponse;

import java.util.Date;
import java.util.List;

/**
 * Response object representing a Company in the system.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CompanyResponse(
        Long companyId,
        String name,
        String RUC,
        String contactEmail,
        String contactPhone,
        List<UserAccountResponse> employees,
        List<AreaCompanyResponse> areaCompanyResponses
) {}
