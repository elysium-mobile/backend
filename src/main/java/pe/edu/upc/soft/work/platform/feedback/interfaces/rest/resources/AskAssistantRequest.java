package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

public record AskAssistantRequest(Long surveyId, String prompt) {
}
