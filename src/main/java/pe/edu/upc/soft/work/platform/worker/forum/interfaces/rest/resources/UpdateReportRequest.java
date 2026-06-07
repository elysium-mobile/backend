package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record UpdateReportRequest(

        @NotNull
        @NotBlank
        String reason,

        @NotNull
        @NotBlank
        @JsonProperty("description")
        String description,

        @NotNull
        @NotBlank
        @JsonProperty("userAccountId")
        Long userAccountId,

        @NotNull
        @NotBlank
        @JsonProperty("reportDate")
        Date reportDate,

        @NotNull
        @NotBlank
        @JsonProperty("areaCompanyId")
        Long areaCompanyId

) {
}
