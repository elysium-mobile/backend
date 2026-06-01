package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Message;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateMessageRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.MessageResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateMessageRequest;
import pe.edu.upc.soft.work.platform.worker.forum.test.fixtures.WorkerForumCommandFixtures;

import static org.assertj.core.api.Assertions.assertThat;

class MessageAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateMessageRequest) -> wraps userAccountId in VO and maps fields (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateMessageRequest(
                WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID,
                WorkerForumCommandFixtures.VALID_MESSAGE_CONTENT);

        // Act
        CreateMessageCommand command = MessageAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.userAccountId().userAccountId())
                .isEqualTo(WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID);
        assertThat(command.contentMessage()).isEqualTo(WorkerForumCommandFixtures.VALID_MESSAGE_CONTENT);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateMessageRequest) -> maps id, wraps userAccountId VO and fields (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateMessageRequest(
                WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID,
                WorkerForumCommandFixtures.VALID_MESSAGE_CONTENT);

        // Act
        UpdateMessageCommand command = MessageAssembler.toCommandFromRequest(61L, request);

        // Assert
        assertThat(command.messageId()).isEqualTo(61L);
        assertThat(command.userAccountId().userAccountId())
                .isEqualTo(WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID);
        assertThat(command.contentMessage()).isEqualTo(WorkerForumCommandFixtures.VALID_MESSAGE_CONTENT);
    }

    @Test
    @DisplayName("toResponseFromEntity(Message) -> unwraps userAccountId VO and maps every field (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new Message(WorkerForumCommandFixtures.validCreateMessageCommand());
        ReflectionTestUtils.setId(entity, 61L);

        // Act
        MessageResponse response = MessageAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.messageId()).isEqualTo(61L);
        assertThat(response.userAccountId()).isEqualTo(WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID);
        assertThat(response.contentMessage()).isEqualTo(WorkerForumCommandFixtures.VALID_MESSAGE_CONTENT);
    }
}
