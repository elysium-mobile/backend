package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.RRHHProfileId;

import java.util.Date;

/**
 * Request object for updating an existing CommentEmployee.
 */
public record UpdateCommentEmployeeRequest(
        @NotNull
        @NotBlank
        String title,
        @NotNull
        @NotBlank
        String content,
        @NotNull
        @NotBlank
        @JsonProperty("rrhhProfileId")
        Long rrhhProfileId,
        @NotNull
        @NotBlank
        @JsonProperty("performanceId")
        Long performanceId
) {}
