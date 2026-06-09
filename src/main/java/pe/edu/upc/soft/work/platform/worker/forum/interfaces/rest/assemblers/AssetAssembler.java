package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Asset;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.AssetResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateAssetRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateAssetRequest;

public class AssetAssembler {

    /**
     * Converts a CreateAttachmentRequest to a CreateAttachmentCommand.
     */
    public static CreateAssetCommand toCommandFromRequest(CreateAssetRequest request) {
        return new CreateAssetCommand(request.messageId(), request.name(), request.url(), request.fileSize(), request.fileType());
    }

    /**
     * Converts an UpdateAttachmentRequest to an UpdateAttachmentCommand.
     */
    public static UpdateAssetCommand toCommandFromRequest(Long attachmentId, UpdateAssetRequest request) {
        return new UpdateAssetCommand(attachmentId, request.messageId(), request.name(), request.url(), request.fileSize(), request.fileType());
    }

    /**
     * Converts an Attachment entity to an AttachmentResponse.
     */
    public static AssetResponse toResponseFromEntity(Asset asset) {
        return new AssetResponse(asset.getId(), asset.getMessageId(), asset.getName(), asset.getUrl(), asset.getFileSize(), asset.getFileType());
    }
}
