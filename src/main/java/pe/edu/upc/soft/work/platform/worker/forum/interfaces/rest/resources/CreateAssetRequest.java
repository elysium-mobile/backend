package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

/**
 * Request object for creating a new Attachment.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateAssetRequest(
        @NotNull
        Long messageId,
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        String url,
        @NotNull
        @NotBlank
        String fileSize,
        @NotNull
        FileType fileType
) {}
