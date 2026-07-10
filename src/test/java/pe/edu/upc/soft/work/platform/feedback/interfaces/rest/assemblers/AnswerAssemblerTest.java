package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.Answer;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.AnswerResponse;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.CreateAnswerRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.UpdateAnswerRequest;
import pe.edu.upc.soft.work.platform.feedback.test.fixtures.FeedbackCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateAnswerRequest) -> maps value and scoreAnswer to CreateAnswerCommand (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateAnswerRequest(
                FeedbackCommandFixtures.VALID_ANSWER_VALUE,
                FeedbackCommandFixtures.VALID_ANSWER_SCORE);

        // Act
        CreateAnswerCommand command = AnswerAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.value()).isEqualTo(FeedbackCommandFixtures.VALID_ANSWER_VALUE);
        assertThat(command.scoreAnswer()).isEqualTo(FeedbackCommandFixtures.VALID_ANSWER_SCORE);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateAnswerRequest) -> maps id, value and scoreAnswer (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateAnswerRequest(
                FeedbackCommandFixtures.VALID_ANSWER_VALUE,
                FeedbackCommandFixtures.VALID_ANSWER_SCORE);

        // Act
        UpdateAnswerCommand command = AnswerAssembler.toCommandFromRequest(21L, request);

        // Assert
        assertThat(command.answerId()).isEqualTo(21L);
        assertThat(command.value()).isEqualTo(FeedbackCommandFixtures.VALID_ANSWER_VALUE);
        assertThat(command.scoreAnswer()).isEqualTo(FeedbackCommandFixtures.VALID_ANSWER_SCORE);
    }

    @Test
    @DisplayName("toResponseFromEntity(Answer) -> maps every field to AnswerResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new Answer(FeedbackCommandFixtures.validCreateAnswerCommand());
        ReflectionTestUtils.setId(entity, 21L);

        // Act
        AnswerResponse response = AnswerAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.answerId()).isEqualTo(21L);
        assertThat(response.value()).isEqualTo(FeedbackCommandFixtures.VALID_ANSWER_VALUE);
        assertThat(response.scoreAnswer()).isEqualTo(FeedbackCommandFixtures.VALID_ANSWER_SCORE);
    }
}
