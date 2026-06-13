package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Asset;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.AssetFactory;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.AssetCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.AssetRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.MessageRepository;

import java.util.Optional;

/**
 * Service implementation for handling Attachment commands
 */
@Service
public class AssetCommandServiceImpl implements AssetCommandService {
    private final AssetRepository assetRepository;
    private final MessageRepository messageRepository;

    /**
     * Constructor for AssetCommandServiceImpl.
     * @param assetRepository the repository for Attachment persistence
     */
    public AssetCommandServiceImpl(AssetRepository assetRepository,
                                   MessageRepository messageRepository) {
        this.assetRepository = assetRepository;
        this.messageRepository=messageRepository;
    }

    /**
     * Handles the creation of an Asset
     * @param command the command to create an Asset
     * @return the generated ID of the new Asset
     */
    @Override
    public Long handle(CreateAssetCommand command) {
        if (!this.assetRepository.existsById(command.messageId())){
            throw new NotFoundArgumentException(
                    String.format("[SurveyResponseCommandServiceImpl] Message ID: %s not found in the external Workers Forum context",
                            command.messageId()));
        }
        var asset = AssetFactory.create(
                command.messageId(),
                command.name(),
                command.url(),
                command.fileSize(),
                command.fileType()
        );
        try {
            assetRepository.save(asset);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Attachment: " + e.getMessage(), e);
        }
        return asset.getId();
    }

    /**
     * Handles the update of an existing Asset
     * @param command the asset to update an Asset
     * @return the updated Asset as an Optional
     */
    @Override
    public Optional<Asset> handle(UpdateAssetCommand command) {
        var assetId = command.assetId();

        var assetToUpdate = assetRepository.findById(assetId)
                .orElseThrow(() -> new NotFoundArgumentException(
                        String.format("[AssetCommandServiceImpl] Attachment ID: %s not found", assetId)));
        if (!this.assetRepository.existsById(assetId)) {
            throw new RuntimeException("Attachment with ID " + assetId + " does not exist.");
        }

        assetToUpdate.updateAsset(
                command.messageId(),
                command.name(),
                command.url(),
                command.fileSize()
        );

        try {
            var updatedAsset = this.assetRepository.save(assetToUpdate);
            return Optional.of(updatedAsset);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Asset: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of an Asset
     * @param command the command to delete an Asset
     */
    @Override
    public void handle(DeleteAssetCommand command) {
        var asset = assetRepository.findById(command.attachmentId())
            .orElseThrow(() -> new RuntimeException("Asset with ID " + command.attachmentId() + " does not exist."));
        var message = messageRepository.findById(asset.getMessageId())
            .orElseThrow(() -> new RuntimeException(
                "[AssetCommandServiceImpl] Message with ID " + asset.getMessageId() + " not found for Asset " + command.attachmentId()));
        try {
            message.removeAttachment(command.attachmentId());
            messageRepository.save(message);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Asset: " + e.getMessage(), e);
        }
    }
}
