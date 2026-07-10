package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.controllers;

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
import jakarta.validation.Valid;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.DeleteCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetCommentEmployeeByIdQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetAllCommentEmployeeQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.services.CommentEmployeeCommandService;
import pe.edu.upc.soft.work.platform.profile.performance.domain.services.CommentEmployeeQueryService;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.assemblers.CommentEmployeeAssembler;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.CreateCommentEmployeeRequest;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.UpdateCommentEmployeeRequest;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.CommentEmployeeResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for managing Comment Employees in the system.
 * Provides endpoints for creating, retrieving, updating, and deleting Comment Employees.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/commentemployees", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Comment Employees", description = "Endpoints for managing CommentEmployees")
public class CommentEmployeeController {

    private final CommentEmployeeCommandService commentemployeeCommandService;
    private final CommentEmployeeQueryService commentemployeeQueryService;

    /**
     * Constructor for CommentEmployeeController.
     * Initializes the command and query services for handling Comment Employee operations.
     * @param commentemployeeCommandService Service for handling commands related to Comment Employees
     * @param commentemployeeQueryService   Service for handling queries related to Comment Employees
     */
    public CommentEmployeeController(CommentEmployeeCommandService commentemployeeCommandService, CommentEmployeeQueryService commentemployeeQueryService) {
        this.commentemployeeCommandService = commentemployeeCommandService;
        this.commentemployeeQueryService = commentemployeeQueryService;
    }

    /**
     * Endpoint for creating a new Comment Employee.
     * @param request Request object containing the details of the Comment Employee to be created
     * @return ResponseEntity containing the created CommentEmployeeResponse and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new CommentEmployee", description = "Create a new CommentEmployee in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CommentEmployee created successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                            schema = @Schema(implementation = CommentEmployeeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "CommentEmployee not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CommentEmployeeResponse> createCommentEmployee(@Valid @RequestBody CreateCommentEmployeeRequest request) {
        var createCommentEmployeeCommand = CommentEmployeeAssembler.toCommandFromRequest(request);
        var commentemployeeId = this.commentemployeeCommandService.handle(createCommentEmployeeCommand);

        if (Objects.isNull(commentemployeeId) || commentemployeeId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getCommentEmployeeById = new GetCommentEmployeeByIdQuery(commentemployeeId);
        var commentemployee = this.commentemployeeQueryService.handle(getCommentEmployeeById);

        if (commentemployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var commentemployeeResponse = CommentEmployeeAssembler.toResponseFromEntity(commentemployee.get());
        return new ResponseEntity<>(commentemployeeResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving all Comment Employees.
     * @return ResponseEntity containing a list of CommentEmployeeResponse objects
     */
    @Operation(summary = "Get all CommentEmployees", description = "Retrieve a list of all CommentEmployees in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CommentEmployees retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CommentEmployeeResponse.class))),
            @ApiResponse(responseCode = "404", description = "No CommentEmployees found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CommentEmployeeResponse>> getAllCommentEmployees() {
        var getAllCommentEmployeeQuery = new GetAllCommentEmployeeQuery();
        var commentemployees = this.commentemployeeQueryService.handle(getAllCommentEmployeeQuery);

        var commentemployeeResponses = commentemployees.stream()
                .map(CommentEmployeeAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentemployeeResponses);
    }

    /**
     * Endpoint for retrieving a specific Comment Employee by ID.
     * @param id ID of the Comment Employee to be retrieved
     * @return ResponseEntity containing the CommentEmployeeResponse if found
     */
    @Operation(summary = "Get CommentEmployee by ID", description = "Retrieve a CommentEmployee by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CommentEmployee retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CommentEmployeeResponse.class))),
            @ApiResponse(responseCode = "404", description = "CommentEmployee not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommentEmployeeResponse> getCommentEmployeeById(@PathVariable Long id) {
        var getCommentEmployeeByIdQuery = new GetCommentEmployeeByIdQuery(id);
        var commentemployee = commentemployeeQueryService.handle(getCommentEmployeeByIdQuery);

        if (commentemployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var commentemployeeResponse = CommentEmployeeAssembler.toResponseFromEntity(commentemployee.get());
        return ResponseEntity.ok(commentemployeeResponse);
    }

    /**
     * Endpoint for updating an existing Comment Employee by ID.
     * @param id ID of the Comment Employee to be updated
     * @param request Request object containing the updated details
     * @return ResponseEntity containing the updated CommentEmployeeResponse if successful
     */
    @Operation(summary = "Update CommentEmployee information", description = "Update the information of an existing CommentEmployee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CommentEmployee updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CommentEmployeeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "CommentEmployee not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CommentEmployeeResponse> updateCommentEmployee(@PathVariable Long id, @Valid @RequestBody UpdateCommentEmployeeRequest request) {
        var updateCommentEmployeeCommand = CommentEmployeeAssembler.toCommandFromRequest(id, request);
        var updatedCommentEmployee = this.commentemployeeCommandService.handle(updateCommentEmployeeCommand);
        if (updatedCommentEmployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var commentemployeeResponse = CommentEmployeeAssembler.toResponseFromEntity(updatedCommentEmployee.get());
        return ResponseEntity.ok(commentemployeeResponse);
    }

    /**
     * Endpoint for deleting a Comment Employee by ID.
     * @param id ID of the Comment Employee to be deleted
     * @return ResponseEntity with no content if deleted successfully
     */
    @Operation(summary = "Delete CommentEmployee by ID", description = "Delete a CommentEmployee by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "CommentEmployee deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "CommentEmployee not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommentEmployeeById(@PathVariable Long id) {
        var deleteCommentEmployeeCommand = new DeleteCommentEmployeeCommand(id);
        this.commentemployeeCommandService.handle(deleteCommentEmployeeCommand);
        return ResponseEntity.noContent().build();
    }
}
