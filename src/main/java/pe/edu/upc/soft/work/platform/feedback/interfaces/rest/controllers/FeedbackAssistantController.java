package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import pe.edu.upc.soft.work.platform.feedback.domain.services.FeedbackAssistantService;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers.AssistantAssembler;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.AskAssistantRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.AssistantAnswerResponse;

/**
 * Controller for managing Feedback Assistant interactions.
 * Provides an endpoint to send prompts to the AI assistant.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.POST})
@RestController
@RequestMapping(value = "/api/v1/feedback-assistant", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Feedback Assistant", description = "AI assistant endpoints for the Feedback context")
public class FeedbackAssistantController {

  private final FeedbackAssistantService feedbackAssistantService;

  /**
   * Constructor for FeedbackAssistantController.
   * Initializes the feedback assistant service.
   * @param feedbackAssistantService Service for handling AI assistant requests
   */
  public FeedbackAssistantController(FeedbackAssistantService feedbackAssistantService) {
    this.feedbackAssistantService = feedbackAssistantService;
  }

  /**
   * Endpoint to send a prompt to the Feedback Assistant.
   * Accepts an AskAssistantRequest and returns the AI-generated answer.
   * @param request Request object containing the user prompt and optional survey context
   * @return ResponseEntity containing the AssistantAnswerResponse
   */
  @Operation(
      summary = "Ask the Feedback Assistant",
      description = "Sends a prompt to the AI assistant, optionally scoped to a survey")
  @PostMapping
  public ResponseEntity<AssistantAnswerResponse> ask(@Valid @RequestBody AskAssistantRequest request) {
    var command = AssistantAssembler.toCommandFromRequest(request);
    var answer = this.feedbackAssistantService.handle(command);
    return ResponseEntity.ok(AssistantAssembler.toResponseFromEntity(answer));
  }

}
