package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.RRHHProfileId;

import java.util.Date;

/**
 * Request object for creating a new CommentEmployee.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateCommentEmployeeRequest(
        @NotNull
        @NotBlank
        String title,
        @NotNull
        @NotBlank
        String content,
        @NotNull
        @NotBlank
        Long rrhhProfileId,
        @NotNull
        @NotBlank
        Long performanceId
) {}
