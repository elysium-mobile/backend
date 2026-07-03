package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReportResponse(
        Long reportId,
        String reason,
        String description,
        Long userAccountId,
        Date reportDate,
        Long areaCompanyId

) {
}
