package pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries;

/**
 * Query to retrieve a Attachment by their unique identifier.
 */
public record GetAssetByIdQuery(Long attachmentId) {

    /**
     * Constructor to validate the assetId parameter.
     */
    public GetAssetByIdQuery {
        if (attachmentId == null || attachmentId <= 0) {
            throw new IllegalArgumentException("Attachment ID must be a positive number.");
        }
    }
}
