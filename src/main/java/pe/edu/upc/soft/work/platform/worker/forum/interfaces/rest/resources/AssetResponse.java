package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

/**
 * Response object representing an Attachment in the system.
 */
public record AssetResponse(
        Long assetId,
        Long messageId,
        String name,
        String url,
        String fileSize,
        FileType fileType,
        boolean isViewable,
        boolean isReadable
) {}
