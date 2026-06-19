package pe.edu.upc.soft.work.platform.feedback.application.internal.outboundservices.acl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.AskFeedbackAssistantCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.AssistantAnswer;
import pe.edu.upc.soft.work.platform.feedback.domain.services.FeedbackAssistantService;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyQueryService;

@Service
public class FeedbackAssistantServiceImpl implements FeedbackAssistantService {

  private final ChatClient chatClient;
  private final SurveyQueryService surveyQueryService;

  public FeedbackAssistantServiceImpl(ChatClient chatClient, SurveyQueryService surveyQueryService) {
    this.chatClient = chatClient;
    this.surveyQueryService = surveyQueryService;
  }

  @Override
  public AssistantAnswer handle(AskFeedbackAssistantCommand command) {
    var contextBuilder = new StringBuilder();
    if (command.surveyId() != null){
      surveyQueryService.handle(new GetSurveyByIdQuery(command.surveyId()))
          .ifPresent(survey -> contextBuilder
              .append("Encuesta: ").append(survey.getTitle())
              .append(" - ").append(survey.getDescription()).append(". "));
    }

    try{
      var response = chatClient.prompt()
          .user(u -> u.text(contextBuilder + command.prompt()))
          .call()
          .content();
      return new AssistantAnswer(response);
    } catch (Exception e) {
      throw new RuntimeException("Error generating assistant response: " + e.getMessage(), e);
    }
  }
}
