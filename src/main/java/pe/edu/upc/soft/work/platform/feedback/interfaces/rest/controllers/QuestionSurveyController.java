package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllQuestionSurveyQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetQuestionSurveyByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.services.QuestionSurveyCommandService;
import pe.edu.upc.soft.work.platform.feedback.domain.services.QuestionSurveyQueryService;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers.QuestionSurveyAssembler;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.CreateQuestionSurveyRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.QuestionSurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.UpdateQuestionSurveyRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing Question-Surveys in the system.
 * Provides endpoints for creating, retrieving, updating, and deleting Question-Surveys.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/question-surveys", produces = "application/json")
@Tag(name = "Question Surveys", description = "Endpoints for managing Question-Surveys")
public class QuestionSurveyController {

    private final QuestionSurveyCommandService questionSurveyCommandService;
    private final QuestionSurveyQueryService questionSurveyQueryService;

    /**
     * Constructor for QuestionSurveyController.
     * Initializes the command and query services for handling Question-Survey operations.
     * @param questionSurveyCommandService Service for handling commands related to Question-Surveys
     * @param questionSurveyQueryService   Service for handling queries related to Question-Surveys
     */
    public QuestionSurveyController(QuestionSurveyCommandService questionSurveyCommandService, QuestionSurveyQueryService questionSurveyQueryService) {
        this.questionSurveyCommandService = questionSurveyCommandService;
        this.questionSurveyQueryService = questionSurveyQueryService;
    }


    /**
     * Endpoint for creating a new Question-Survey.
     * @param request Request object containing the details of the Question-Survey to be created
     * @return ResponseEntity containing the created Question-Survey and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new Question-Survey", description = "Create a new Question-Survey in the system")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Question-Survey created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Question-Survey not found")
    })
    @PostMapping
    public ResponseEntity<QuestionSurveyResponse> createQuestionSurvey(@RequestBody CreateQuestionSurveyRequest request){
        var createQuestionSurveyCommand = QuestionSurveyAssembler.toCommandFromRequest(request);
        var questionSurveyId = this.questionSurveyCommandService.handle(createQuestionSurveyCommand);

        if (questionSurveyId == null || questionSurveyId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var questionSurveyById = new GetQuestionSurveyByIdQuery(questionSurveyId);
        var questionSurvey = this.questionSurveyQueryService.handle(questionSurveyById);

        if (questionSurvey.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var questionSurveyResponse = QuestionSurveyAssembler.toResponseFromEntity(questionSurvey.get());
        return new ResponseEntity<>(questionSurveyResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving all Question-Surveys.
     * @return ResponseEntity containing a list of QuestionSurveyResponse objects
     */
    @Operation(summary = "Get all Question-Surveys", description = "Retrieve a list of all Question-Surveys")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Question-Surveys retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No Question-Surveys found")
    })
    @GetMapping
    public ResponseEntity<List<QuestionSurveyResponse>> getAllQuestionSurveys(){;
        var getAllQuestionSurveyQuery = new GetAllQuestionSurveyQuery();
        var questionSurveys = this.questionSurveyQueryService.handle(getAllQuestionSurveyQuery);

        if (questionSurveys.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var questionSurveyResponses = questionSurveys.stream()
                .map(QuestionSurveyAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(questionSurveyResponses);
    }

    /**
     * Endpoint for retrieving a specific Question-Survey by its ID.
     * @param id ID of the Question-Survey to be retrieved
     * @return ResponseEntity containing the QuestionSurveyResponse if found
     */
    @Operation(summary = "Get a Question-Survey by ID", description = "Retrieve a Question-Survey by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Question-Survey retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Question-Survey not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<QuestionSurveyResponse> getQuestionByServiceById(@PathVariable Long id){
        var getQuestionSurveyById = new GetQuestionSurveyByIdQuery(id);
        var questionSurvey = this.questionSurveyQueryService.handle(getQuestionSurveyById);

        if (questionSurvey.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var questionSurveyResponse = QuestionSurveyAssembler.toResponseFromEntity(questionSurvey.get());
        return ResponseEntity.ok(questionSurveyResponse);
    }


    /**
     * Endpoint for updating an existing Question-Survey by its ID.
     * @param id ID of the Question-Survey to be updated
     * @param request Request object containing the updated details
     * @return ResponseEntity containing the updated QuestionSurveyResponse if successful
     */
    @Operation(summary = "Update a Question-Survey by ID", description = "Update a Question-Survey by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Question-Survey updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Question-Survey not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<QuestionSurveyResponse> updateQuestionSurvey(@PathVariable Long id, @RequestBody UpdateQuestionSurveyRequest request){
        var updateQuestionSurveyCommand = QuestionSurveyAssembler.toCommandFromRequest(id, request);
        var updatedQuestionSurvey = this.questionSurveyCommandService.handle(updateQuestionSurveyCommand);
        if (updatedQuestionSurvey.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var questionSurveyResponse = QuestionSurveyAssembler.toResponseFromEntity(updatedQuestionSurvey.get());
        return ResponseEntity.ok(questionSurveyResponse);
    }

    /**
     * Endpoint for deleting an existing Question-Survey by its ID.
     * @param id ID of the Question-Survey to be deleted
     * @return ResponseEntity with no content if deleted successfully
     */
    @Operation(summary = "Delete a Question-Survey by ID", description = "Delete a Question-Survey by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Question-Survey deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Question-Survey not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestionSurveyById(@PathVariable Long id){
        var deleteQuestionSurveyCommand = new DeleteQuestionSurveyCommand(id);
        this.questionSurveyCommandService.handle(deleteQuestionSurveyCommand);
        return ResponseEntity.noContent().build();
    }

}
