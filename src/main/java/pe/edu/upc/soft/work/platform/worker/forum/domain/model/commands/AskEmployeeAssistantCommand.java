package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

/**
 * Command to ask the Employee Assistant a question or concern about the company.
 *
 * @param companyId optional ID of the employee's company, used to enrich the AI context
 * @param prompt    the question or concern raised by the employee
 */
public record AskEmployeeAssistantCommand(Long companyId, String prompt) {

  public AskEmployeeAssistantCommand {
    if (prompt == null || prompt.isBlank()) {
      throw new IllegalArgumentException("Prompt must not be empty.");
    }
  }
}
