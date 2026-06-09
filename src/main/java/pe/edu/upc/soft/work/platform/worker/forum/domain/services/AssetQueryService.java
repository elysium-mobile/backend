package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Asset;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAssetByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllAssetsQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Attachments in the system.
 */
public interface AssetQueryService {

    /**
     * Retrieves a list of all Attachments in the system.
     */
    List<Asset> handle(GetAllAssetsQuery query);

    /**
     * Retrieves a Attachment by their unique identifier.
     */
    Optional<Asset> handle(GetAssetByIdQuery query);
}
