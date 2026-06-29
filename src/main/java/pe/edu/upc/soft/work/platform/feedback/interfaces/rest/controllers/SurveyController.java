package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllSurveyQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyCommandService;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyQueryService;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers.SurveyAssembler;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.CreateSurveyRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.SurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.UpdateSurveyRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for managing Surveys in the system.
 * Provides endpoints for creating, retrieving, updating, and deleting Surveys.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/surveys", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Surveys", description = "Endpoints for managing Surveys")
public class SurveyController {

    private final SurveyCommandService surveyCommandService;
    private final SurveyQueryService surveyQueryService;

    /**
     * Constructor for SurveyController.
     * Initializes the command and query services for handling Survey operations.
     * @param surveyCommandService Service for handling commands related to Surveys
     * @param surveyQueryService   Service for handling queries related to Surveys
     */
    public SurveyController(SurveyCommandService surveyCommandService, SurveyQueryService surveyQueryService) {
        this.surveyCommandService = surveyCommandService;
        this.surveyQueryService = surveyQueryService;
    }

    /**
     * Endpoint for creating a new Survey.
     * @param request Request object containing the details of the Survey to be created
     * @return ResponseEntity containing the created Survey and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new Survey", description = "Create a new Survey in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Survey created successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                            schema = @Schema(implementation = SurveyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Survey not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<SurveyResponse> createSurvey(@RequestBody CreateSurveyRequest request) {
        var createSurveyCommand = SurveyAssembler.toCommandFromRequest(request);
        var surveyId = this.surveyCommandService.handle(createSurveyCommand);

        if (Objects.isNull(surveyId) || surveyId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getSurveyById = new GetSurveyByIdQuery(surveyId);
        var survey = this.surveyQueryService.handle(getSurveyById);

        if (survey.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var surveyResponse = SurveyAssembler.toResponseFromEntity(survey.get());
        return new ResponseEntity<>(surveyResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving all Surveys.
     * @return ResponseEntity containing a list of SurveyResponse objects
     */
    @Operation(summary = "Get all Surveys", description = "Retrieve a list of all Surveys in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Surveys retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SurveyResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Surveys found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<SurveyResponse>> getAllSurveys() {
        var getAllSurveyQuery = new GetAllSurveyQuery();
        var surveys = this.surveyQueryService.handle(getAllSurveyQuery);

        var surveyResponses = surveys.stream()
                .map(SurveyAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(surveyResponses);
    }

    /**
     * Endpoint for retrieving a specific Survey by its ID.
     * @param id ID of the Survey to be retrieved
     * @return ResponseEntity containing the SurveyResponse if found
     */
    @Operation(summary = "Get Survey by ID", description = "Retrieve a Survey by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Survey retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SurveyResponse.class))),
            @ApiResponse(responseCode = "404", description = "Survey not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SurveyResponse> getSurveyById(@PathVariable Long id) {
        var getSurveyByIdQuery = new GetSurveyByIdQuery(id);
        var survey = surveyQueryService.handle(getSurveyByIdQuery);

        if (survey.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var surveyResponse = SurveyAssembler.toResponseFromEntity(survey.get());
        return ResponseEntity.ok(surveyResponse);
    }

    /**
     * Endpoint for updating an existing Survey by its ID.
     * @param id ID of the Survey to be updated
     * @param request Request object containing the updated details
     * @return ResponseEntity containing the updated SurveyResponse if successful
     */
    @Operation(summary = "Update Survey information", description = "Update the information of an existing Survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Survey updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SurveyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Survey not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SurveyResponse> updateSurvey(@PathVariable Long id, @RequestBody UpdateSurveyRequest request) {
        var updateSurveyCommand = SurveyAssembler.toCommandFromRequest(id, request);
        var updatedSurvey = this.surveyCommandService.handle(updateSurveyCommand);
        if (updatedSurvey.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var surveyResponse = SurveyAssembler.toResponseFromEntity(updatedSurvey.get());
        return ResponseEntity.ok(surveyResponse);
    }

    /**
     * Endpoint for deleting an existing Survey by its ID.
     * @param id ID of the Survey to be deleted
     * @return ResponseEntity with no content if deleted successfully
     */
    @Operation(summary = "Delete Survey by ID", description = "Delete a Survey by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Survey deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Survey not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSurveyById(@PathVariable Long id) {
        var deleteSurveyCommand = new DeleteSurveyCommand(id);
        this.surveyCommandService.handle(deleteSurveyCommand);
        return ResponseEntity.noContent().build();
    }
}
