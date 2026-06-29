package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllAnswerQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAnswerByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.services.AnswerCommandService;
import pe.edu.upc.soft.work.platform.feedback.domain.services.AnswerQueryService;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers.AnswerAssembler;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.AnswerResponse;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.CreateAnswerRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.UpdateAnswerRequest;

import java.util.List;
import java.util.Objects;

/**
 * Controller for managing Answers in the system.
 * Provides endpoints for creating, retrieving, updating, and deleting Answers.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/answers", produces = "application/json")
@Tag(name = "Answers", description = "Endpoints for managing Answers")
public class AnswerController {

    private final AnswerCommandService answerCommandService;
    private final AnswerQueryService answerQueryService;

    /**
     * Constructor for AnswerController.
     * Initializes the command and query services for handling Answer operations.
     * @param answerCommandService Service for handling commands related to Answers
     * @param answerQueryService   Service for handling queries related to Answers
     */
    public AnswerController(AnswerCommandService answerCommandService, AnswerQueryService answerQueryService){
        this.answerCommandService = answerCommandService;
        this.answerQueryService = answerQueryService;
    }

    /**
     * Endpoint for creating a new Answer.
     * Accepts a CreateAnswerRequest object and returns the created Answer.
     * @param request Request object containing the details of the Answer to be created
     * @return ResponseEntity containing the created Answer and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new Answer", description = "Create a new Answer in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Answer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Answer not found")
    })
    @PostMapping
    public ResponseEntity<AnswerResponse> createAnswer(@RequestBody CreateAnswerRequest request){
        var createAnswerCommand = AnswerAssembler.toCommandFromRequest(request);
        var answerId = this.answerCommandService.handle(createAnswerCommand);

        if (Objects.isNull(answerId) || answerId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getAnswerById = new GetAnswerByIdQuery(answerId);
        var answer = this.answerQueryService.handle(getAnswerById);

        if (answer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var answerResponse = AnswerAssembler.toResponseFromEntity(answer.get());
        return new ResponseEntity<>(answerResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving all Answers.
     * Returns a list of AnswerResponse objects.
     * @return ResponseEntity containing a list of AnswerResponse objects
     */
    @Operation(summary = "Get all Answers", description = "Retrieve a list of all Answers in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Answers retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No Answers found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<AnswerResponse>> getAllAnswers(){
        var getAllAnswersQuery = new GetAllAnswerQuery();
        var answers = this.answerQueryService.handle(getAllAnswersQuery);

        var answersResponse= answers.stream()
                .map(AnswerAssembler::toResponseFromEntity)
                .toList();
        return ResponseEntity.ok(answersResponse);
    }

    /**
     * Endpoint for retrieving a specific Answer by its ID.
     * @param id ID of the Answer to be retrieved
     * @return ResponseEntity containing the AnswerResponse if found
     */
    @Operation(summary = "Get Answer by ID", description = "Retrieve a specific Answer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Answer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Answer not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponse> getAnswerById(@PathVariable Long id){
        var getAnswerByIdQuery = new GetAnswerByIdQuery(id);
        var answer = this.answerQueryService.handle(getAnswerByIdQuery);

        if (answer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var answerResponse = AnswerAssembler.toResponseFromEntity(answer.get());
        return ResponseEntity.ok(answerResponse);
    }

    /**
     * Endpoint for updating an existing Answer by its ID.
     * @param id ID of the Answer to be updated
     * @param request Request object containing the updated details
     * @return ResponseEntity containing the updated AnswerResponse if successful
     */
    @Operation(summary = "Update an existing Answer", description = "Update the details of an existing Answer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Answer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Answer not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponse> updateAnswer(@PathVariable Long id, @RequestBody UpdateAnswerRequest request){
        var updateAnswerCommand = AnswerAssembler.toCommandFromRequest(id,request);
        var updatedAnswer = this.answerCommandService.handle(updateAnswerCommand);
        if (updatedAnswer.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var answerResponse = AnswerAssembler.toResponseFromEntity(updatedAnswer.get());
        return ResponseEntity.ok(answerResponse);
    }

    /**
     * Endpoint for deleting an existing Answer by its ID.
     * @param id ID of the Answer to be deleted
     * @return ResponseEntity with no content if deleted successfully
     */
    @Operation(summary = "Delete an Answer", description = "Delete an existing Answer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Answer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Answer not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnswerById(@PathVariable Long id){
        var deleteAnswerCommand = new DeleteAnswerCommand(id);
        this.answerCommandService.handle(deleteAnswerCommand);
        return ResponseEntity.noContent().build();
    }


}
