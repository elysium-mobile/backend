package pe.edu.upc.soft.work.platform.feedback.domain.services;

import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.AskFeedbackAssistantCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.AssistantAnswer;

public interface FeedbackAssistantService
{
  AssistantAnswer handle(AskFeedbackAssistantCommand command);
}
