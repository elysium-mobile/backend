package pe.edu.upc.soft.work.platform.worker.forum.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Asset;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAssetByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllAssetsQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.AssetQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.AssetRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the AttachmentQueryService interface.
 */
@Service
public class AssetQueryServiceImpl implements AssetQueryService {
    private final AssetRepository assetRepository;

    /**
     * Constructor for AttachmentQueryServiceImpl.
     */
    public AssetQueryServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    /**
     * Handles the GetAllAttachmentQuery.
     */
    @Override
    public List<Asset> handle(GetAllAssetsQuery query) {
        return assetRepository.findAll();
    }

    /**
     * Handles the GetAttachmentByIdQuery.
     */
    @Override
    public Optional<Asset> handle(GetAssetByIdQuery query) {
        return assetRepository.findById(query.attachmentId());
    }
}
