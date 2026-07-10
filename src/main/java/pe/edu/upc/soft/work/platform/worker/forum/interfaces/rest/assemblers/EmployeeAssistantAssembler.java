package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.AskEmployeeAssistantCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.AssistantAnswer;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.AskEmployeeAssistantRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.AssistantAnswerResponse;

/**
 * Assembler for converting between REST resources and domain objects
 * of the Employee Assistant feature.
 */
public class EmployeeAssistantAssembler {

  public static AskEmployeeAssistantCommand toCommandFromRequest(AskEmployeeAssistantRequest request) {
    return new AskEmployeeAssistantCommand(request.companyId(), request.prompt());
  }

  public static AssistantAnswerResponse toResponseFromEntity(AssistantAnswer answer) {
    return new AssistantAnswerResponse(answer.contentAnswer());
  }
}
