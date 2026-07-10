package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.DashboardAssistantService;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers.DashboardAssistantAssembler;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.AnalyzeDashboardRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.DashboardInsightResponse;

/**
 * Controller for the Dashboard Assistant.
 * Lets RRHH managers ask the AI (Gemini) to examine a company's dashboard
 * metrics (performance, survey sentiment, reports, forum activity) and get a
 * diagnosis of the work climate along with actionable recommendations.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.POST})
@RestController
@RequestMapping(value = "/api/v1/dashboard-assistant", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Dashboard Assistant", description = "AI assistant endpoint for RRHH to analyze dashboard/climate metrics of a company")
public class DashboardAssistantController {

  private final DashboardAssistantService dashboardAssistantService;

  /**
   * Constructor for DashboardAssistantController.
   *
   * @param dashboardAssistantService service for handling dashboard AI analysis requests
   */
  public DashboardAssistantController(DashboardAssistantService dashboardAssistantService) {
    this.dashboardAssistantService = dashboardAssistantService;
  }

  /**
   * Endpoint to analyze the dashboard metrics of a company.
   * Accepts an AnalyzeDashboardRequest and returns the AI-generated diagnosis.
   *
   * @param request Request object containing the company ID and an optional follow-up question
   * @return ResponseEntity containing the DashboardInsightResponse
   */
  @Operation(
      summary = "Analyze a company's dashboard",
      description = "Gathers the company's climate metrics and asks the AI assistant for a diagnosis and recommendations")
  @PostMapping
  public ResponseEntity<DashboardInsightResponse> analyze(@RequestBody AnalyzeDashboardRequest request) {
    var command = DashboardAssistantAssembler.toCommandFromRequest(request);
    var insight = this.dashboardAssistantService.handle(command);
    return ResponseEntity.ok(DashboardAssistantAssembler.toResponseFromEntity(insight));
  }
}
