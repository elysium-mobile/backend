package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.RRHHProfileId;

import java.util.Date;

/**
 * Request object for creating a new CommentEmployee.
 */
public record CreateCommentEmployeeRequest(
        @NotNull
        @NotBlank
        String title,
        @NotNull
        @NotBlank
        String content,
        @NotNull
        @NotBlank
        @JsonProperty("rrhhProfileId")
        Long rrhhProfileId
) {}
