package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Asset;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteAssetCommand;

import java.util.Optional;

/**
 * Service interface for handling Attachment-related commands.
 */
public interface AssetCommandService {

    /**
     * Handles the creation of a new Attachment.
     */
    Long handle(CreateAssetCommand command);

    /**
     * Handles the update of an existing Attachment.
     */
    Optional<Asset> handle(UpdateAssetCommand command);

    /**
     * Handles the deletion of an existing Attachment.
     */
    void handle(DeleteAssetCommand command);
}
