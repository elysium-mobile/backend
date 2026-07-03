package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateReportRequest(

        @NotNull
        @NotBlank
        String reason,

        @NotNull
        @NotBlank
        String description,

        @NotNull
        @NotBlank
        Long userAccountId,

        @NotNull
        @NotBlank
        Date reportDate,

        @NotNull
        @NotBlank
        Long areaCompanyId

) {
}
