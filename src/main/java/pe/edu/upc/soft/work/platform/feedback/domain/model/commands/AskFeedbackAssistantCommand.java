package pe.edu.upc.soft.work.platform.feedback.domain.model.commands;

public record AskFeedbackAssistantCommand(Long surveyId, String prompt) {

  public AskFeedbackAssistantCommand {
    if (prompt == null || prompt.isBlank()) {
      throw new IllegalArgumentException("Prompt must not be empty.");
    }
  }
}
