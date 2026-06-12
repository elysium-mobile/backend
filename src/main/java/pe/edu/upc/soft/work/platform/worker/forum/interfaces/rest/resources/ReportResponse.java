package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import java.util.Date;

public record ReportResponse(
        Long reportId,
        String reason,
        String description,
        Long userAccountId,
        Date reportDate,
        Long areaCompanyId

) {
}
