package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Asset;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.AssetFactory;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.AssetResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateAssetRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateAssetRequest;
import pe.edu.upc.soft.work.platform.worker.forum.test.fixtures.WorkerForumCommandFixtures;

import static org.assertj.core.api.Assertions.assertThat;

class AssetAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateAttachmentRequest) -> maps every field to CreateAttachmentCommand (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateAssetRequest(
                WorkerForumCommandFixtures.VALID_MESSAGE_ID,
                WorkerForumCommandFixtures.VALID_ATTACHMENT_NAME,
                WorkerForumCommandFixtures.VALID_ATTACHMENT_URL,
                WorkerForumCommandFixtures.VALID_FILE_SIZE,
                WorkerForumCommandFixtures.VALID_FILE_TYPE);

        // Act
        CreateAssetCommand command = AssetAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.messageId()).isEqualTo(WorkerForumCommandFixtures.VALID_MESSAGE_ID);
        assertThat(command.name()).isEqualTo(WorkerForumCommandFixtures.VALID_ATTACHMENT_NAME);
        assertThat(command.url()).isEqualTo(WorkerForumCommandFixtures.VALID_ATTACHMENT_URL);
        assertThat(command.fileSize()).isEqualTo(WorkerForumCommandFixtures.VALID_FILE_SIZE);
        assertThat(command.fileType()).isEqualTo(WorkerForumCommandFixtures.VALID_FILE_TYPE);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateAttachmentRequest) -> maps id and all fields to UpdateAttachmentCommand (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateAssetRequest(
                WorkerForumCommandFixtures.VALID_MESSAGE_ID,
                WorkerForumCommandFixtures.VALID_ATTACHMENT_NAME,
                WorkerForumCommandFixtures.VALID_ATTACHMENT_URL,
                WorkerForumCommandFixtures.VALID_FILE_SIZE);

        // Act
        UpdateAssetCommand command = AssetAssembler.toCommandFromRequest(31L, request);

        // Assert
        assertThat(command.attachmentId()).isEqualTo(31L);
        assertThat(command.messageId()).isEqualTo(WorkerForumCommandFixtures.VALID_MESSAGE_ID);
        assertThat(command.name()).isEqualTo(WorkerForumCommandFixtures.VALID_ATTACHMENT_NAME);
        assertThat(command.url()).isEqualTo(WorkerForumCommandFixtures.VALID_ATTACHMENT_URL);
        assertThat(command.fileSize()).isEqualTo(WorkerForumCommandFixtures.VALID_FILE_SIZE);
    }

    @Test
    @DisplayName("toResponseFromEntity(Attachment) -> maps every field to AttachmentResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange — usamos AssetFactory en vez de new Asset(command)
        var command = WorkerForumCommandFixtures.validCreateAssetCommand();
        var entity = AssetFactory.create(
            command.messageId(),
            command.name(),
            command.url(),
            command.fileSize(),
            command.fileType()
        );
        ReflectionTestUtils.setId(entity, 31L);

        // Act
        AssetResponse response = AssetAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.attachmentId()).isEqualTo(31L);
        assertThat(response.messageId()).isEqualTo(WorkerForumCommandFixtures.VALID_MESSAGE_ID);
        assertThat(response.name()).isEqualTo(WorkerForumCommandFixtures.VALID_ATTACHMENT_NAME);
        assertThat(response.url()).isEqualTo(WorkerForumCommandFixtures.VALID_ATTACHMENT_URL);
        assertThat(response.fileSize()).isEqualTo(WorkerForumCommandFixtures.VALID_FILE_SIZE);
        assertThat(response.fileType()).isEqualTo(WorkerForumCommandFixtures.VALID_FILE_TYPE);
        assertThat(response.isViewable()).isEqualTo(WorkerForumCommandFixtures.VALID_FILE_TYPE_IS_VIEWABLE);
        assertThat(response.isReadable()).isEqualTo(WorkerForumCommandFixtures.VALID_FILE_TYPE_IS_READABLE);
    }
}
