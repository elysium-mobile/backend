package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.AskFeedbackAssistantCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.AssistantAnswer;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.AskAssistantRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.AssistantAnswerResponse;

public class AssistantAssembler {
  public static AskFeedbackAssistantCommand toCommandFromRequest(AskAssistantRequest request) {
    return new AskFeedbackAssistantCommand(request.surveyId(), request.prompt());
  }

  public static AssistantAnswerResponse toResponseFromEntity(AssistantAnswer answer) {
    return new AssistantAnswerResponse(answer.contentAnswer());
  }

}
