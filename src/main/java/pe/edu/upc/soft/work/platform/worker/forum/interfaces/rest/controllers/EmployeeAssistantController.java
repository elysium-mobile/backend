package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.EmployeeAssistantService;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers.EmployeeAssistantAssembler;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.AskEmployeeAssistantRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.AssistantAnswerResponse;

/**
 * Controller for the Employee Assistant chatbot.
 * Lets employees chat with an AI (Gemini) about general concerns or
 * questions regarding the company (policies, benefits, work climate, etc.).
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.POST})
@RestController
@RequestMapping(value = "/api/v1/employee-assistant", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Employee Assistant", description = "AI chatbot endpoint for employees to ask questions/concerns about the company")
public class EmployeeAssistantController {

  private final EmployeeAssistantService employeeAssistantService;

  /**
   * Constructor for EmployeeAssistantController.
   *
   * @param employeeAssistantService service for handling employee AI assistant requests
   */
  public EmployeeAssistantController(EmployeeAssistantService employeeAssistantService) {
    this.employeeAssistantService = employeeAssistantService;
  }

  /**
   * Endpoint to send a prompt to the Employee Assistant.
   * Accepts an AskEmployeeAssistantRequest and returns the AI-generated answer.
   *
   * @param request Request object containing the employee's prompt and optional company ID
   * @return ResponseEntity containing the AssistantAnswerResponse
   */
  @Operation(
      summary = "Ask the Employee Assistant",
      description = "Sends a prompt/concern to the AI assistant, optionally scoped to the employee's company")
  @PostMapping
  public ResponseEntity<AssistantAnswerResponse> ask(@RequestBody AskEmployeeAssistantRequest request) {
    var command = EmployeeAssistantAssembler.toCommandFromRequest(request);
    var answer = this.employeeAssistantService.handle(command);
    return ResponseEntity.ok(EmployeeAssistantAssembler.toResponseFromEntity(answer));
  }
}
