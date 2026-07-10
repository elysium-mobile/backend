package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.controllers;

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
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllForumQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetForumByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetForumsByCompanyIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ForumCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ForumQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers.ForumAssembler;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.AddCategoryToForumRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateForumRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.ForumResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateForumRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for managing Forums in the system.
 * Provides endpoints for creating, retrieving, updating, and deleting Forums,
 * as well as managing associated categories and filtering by company.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/forums", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Forums", description = "Endpoints for managing Forums")
public class ForumController {

    private final ForumCommandService forumCommandService;
    private final ForumQueryService forumQueryService;

    /**
     * Constructor for ForumController.
     * Initializes the command and query services for handling Forum operations.
     * @param forumCommandService Service for handling commands related to Forums
     * @param forumQueryService   Service for handling queries related to Forums
     */
    public ForumController(ForumCommandService forumCommandService, ForumQueryService forumQueryService) {
        this.forumCommandService = forumCommandService;
        this.forumQueryService = forumQueryService;
    }

    /**
     * Endpoint for creating a new Forum.
     * @param request Request object containing the details of the Forum to be created
     * @return ResponseEntity containing the created ForumResponse and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new Forum", description = "Create a new Forum in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Forum created successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                            schema = @Schema(implementation = ForumResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Forum not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ForumResponse> createForum(@Valid @RequestBody CreateForumRequest request) {
        var createForumCommand = ForumAssembler.toCommandFromRequest(request);
        var forumId = this.forumCommandService.handle(createForumCommand);

        if (Objects.isNull(forumId) || forumId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getForumById = new GetForumByIdQuery(forumId);
        var forum = this.forumQueryService.handle(getForumById);

        if (forum.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var forumResponse = ForumAssembler.toResponseFromEntity(forum.get());
        return new ResponseEntity<>(forumResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving all Forums.
     * @return ResponseEntity containing a list of ForumResponse objects
     */
    @Operation(summary = "Get all Forums", description = "Retrieve a list of all Forums in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forums retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ForumResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Forums found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ForumResponse>> getAllForums() {
        var getAllForumQuery = new GetAllForumQuery();
        var forums = this.forumQueryService.handle(getAllForumQuery);

        var forumResponses = forums.stream()
                .map(ForumAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(forumResponses);
    }

    /**
     * Endpoint for retrieving a specific Forum by ID.
     * @param id ID of the Forum to be retrieved
     * @return ResponseEntity containing the ForumResponse if found
     */
    @Operation(summary = "Get Forum by ID", description = "Retrieve a Forum by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forum retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ForumResponse.class))),
            @ApiResponse(responseCode = "404", description = "Forum not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ForumResponse> getForumById(@PathVariable Long id) {
        var getForumByIdQuery = new GetForumByIdQuery(id);
        var forum = forumQueryService.handle(getForumByIdQuery);

        if (forum.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var forumResponse = ForumAssembler.toResponseFromEntity(forum.get());
        return ResponseEntity.ok(forumResponse);
    }

    /**
     * Endpoint for updating an existing Forum by ID.
     * @param id ID of the Forum to be updated
     * @param request Request object containing the updated details
     * @return ResponseEntity containing the updated ForumResponse if successful
     */
    @Operation(summary = "Update Forum information", description = "Update the information of an existing Forum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forum updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ForumResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Forum not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ForumResponse> updateForum(@PathVariable Long id, @Valid @RequestBody UpdateForumRequest request) {
        var updateForumCommand = ForumAssembler.toCommandFromRequest(id, request);
        var updatedForum = this.forumCommandService.handle(updateForumCommand);
        if (updatedForum.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var forumResponse = ForumAssembler.toResponseFromEntity(updatedForum.get());
        return ResponseEntity.ok(forumResponse);
    }

    /**
     * Endpoint for deleting a Forum by ID.
     * @param id ID of the Forum to be deleted
     * @return ResponseEntity with no content if deleted successfully
     */
    @Operation(summary = "Delete Forum by ID", description = "Delete a Forum by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Forum deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Forum not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteForumById(@PathVariable Long id) {
        var deleteForumCommand = new DeleteForumCommand(id);
        this.forumCommandService.handle(deleteForumCommand);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for adding a Category to a Forum.
     * @param forumId ID of the Forum
     * @param request Request object containing the Category details
     * @return ResponseEntity containing the updated ForumResponse
     */
    @Operation(summary = "Add Category to Forum", description = "Add a Category to an existing Forum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category added to Forum successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ForumResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Forum or Category not found", content = @Content)
    })
    @PostMapping("/{forumId}/categories")
    public ResponseEntity<ForumResponse> addCategoryToForum(@PathVariable Long forumId, @Valid @RequestBody AddCategoryToForumRequest request){
        var command = ForumAssembler.toCommandFromRequest(forumId, request);
        this.forumCommandService.handle(command);
        var forum = this.forumQueryService.handle(new GetForumByIdQuery(forumId));
        if (forum.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var forumResponse = ForumAssembler.toResponseFromEntity(forum.get());
        return ResponseEntity.ok(forumResponse);
    }

    /**
     * Endpoint for retrieving Forums by Company ID.
     * @param companyId ID of the Company
     * @return ResponseEntity containing a list of ForumResponse objects
     */
    @Operation(summary = "Get Forums by Company ID", description = "Retrieve a list of Forums associated with a specific Company ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forums retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ForumResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Forums found for the given Company ID", content = @Content)
    })
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<ForumResponse>> getForumsByCompanyId(@PathVariable Long companyId) {
        var getForumsByCompanyIdQuery = new GetForumsByCompanyIdQuery(companyId);
        var forums = this.forumQueryService.handle(getForumsByCompanyIdQuery);

        if (forums.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var forumResponses = forums.stream()
                .map(ForumAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(forumResponses);
    }
}
