package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record AddCommentEmployeeToPerformanceRequest(
        @NotNull
        @JsonProperty("commentId")
        Long commentId
) {
}
