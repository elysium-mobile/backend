package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

/**
 * Response object representing an Attachment in the system.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AssetResponse(
        Long assetId,
        Long messageId,
        String name,
        String url,
        String fileSize,
        FileType fileType
) {}
