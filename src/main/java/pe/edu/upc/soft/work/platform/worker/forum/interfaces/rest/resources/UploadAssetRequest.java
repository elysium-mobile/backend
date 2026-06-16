package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

public record UploadAssetRequest(
    Long messageId,
    String name,
    String fileType
) {
}
