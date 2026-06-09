package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Asset;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteAssetCommand;
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
     * Constructor for AttachmentCommandServiceImpl.
     * @param assetRepository the repository for Attachment persistence
     */
    public AssetCommandServiceImpl(AssetRepository assetRepository,
                                   MessageRepository messageRepository) {
        this.assetRepository = assetRepository;
        this.messageRepository=messageRepository;
    }

    /**
     * Handles the creation of an Attachment
     * @param command the command to create an Attachment
     * @return the generated ID of the new Attachment
     */
    @Override
    public Long handle(CreateAssetCommand command) {
        if (!this.assetRepository.existsById(command.messageId())){
            throw new NotFoundArgumentException(
                    String.format("[SurveyResponseCommandServiceImpl] Message ID: %s not found in the external Workers Forum context",
                            command.messageId()));
        }
        var attachment = new Asset(command);
        try {
            assetRepository.save(attachment);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Attachment: " + e.getMessage(), e);
        }
        return attachment.getId();
    }

    /**
     * Handles the update of an existing Attachment
     * @param command the command to update an Attachment
     * @return the updated Attachment as an Optional
     */
    @Override
    public Optional<Asset> handle(UpdateAssetCommand command) {
        var attachmentId = command.attachmentId();
        if (!this.assetRepository.existsById(attachmentId)) {
            throw new RuntimeException("Attachment with ID " + attachmentId + " does not exist.");
        }

        var attachmentToUpdate = this.assetRepository.findById(attachmentId).get();
        attachmentToUpdate.updateAttachment(command);
        try {
            var updatedAttachment = this.assetRepository.save(attachmentToUpdate);
            return Optional.of(updatedAttachment);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Attachment: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of an Attachment
     * @param command the command to delete an Attachment
     */
    @Override
    public void handle(DeleteAssetCommand command) {
        if (!assetRepository.existsById(command.attachmentId())) {
            throw new RuntimeException("Attachment with ID " + command.attachmentId() + " does not exist.");
        }
        try {
            assetRepository.deleteById(command.attachmentId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Attachment: " + e.getMessage(), e);
        }
    }
}
