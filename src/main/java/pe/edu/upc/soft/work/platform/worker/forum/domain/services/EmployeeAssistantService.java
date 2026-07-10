package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.AskEmployeeAssistantCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.AssistantAnswer;

/**
 * Service interface for handling employee questions/concerns through the AI assistant.
 */
public interface EmployeeAssistantService {

  /**
   * Handles a question asked by an employee.
   *
   * @param command the command containing the employee's prompt and optional company context
   * @return the AI-generated answer
   */
  AssistantAnswer handle(AskEmployeeAssistantCommand command);
}
