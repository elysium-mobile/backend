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
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllThreadQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetThreadByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetThreadsByAreaCompanyIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ThreadCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ThreadQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers.ThreadAssembler;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.AddMessageToThreadRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateThreadRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.ThreadResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateThreadRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Controller for managing Threads in the system.
 * Provides endpoints for creating, retrieving, updating, and deleting Threads,
 * as well as managing associated messages and filtering by area company.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/threads", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Threads", description = "Endpoints for managing Threads")
public class ThreadController {

    private final ThreadCommandService threadCommandService;
    private final ThreadQueryService threadQueryService;

    /**
     * Constructor for ThreadController.
     * Initializes the command and query services for handling Thread operations.
     * @param threadCommandService Service for handling commands related to Threads
     * @param threadQueryService   Service for handling queries related to Threads
     */
    public ThreadController(ThreadCommandService threadCommandService, ThreadQueryService threadQueryService) {
        this.threadCommandService = threadCommandService;
        this.threadQueryService = threadQueryService;
    }

    /**
     * Endpoint for creating a new Thread.
     * @param request Request object containing the details of the Thread to be created
     * @return ResponseEntity containing the created ThreadResponse and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new Thread", description = "Create a new Thread in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Thread created successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                            schema = @Schema(implementation = ThreadResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Thread not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ThreadResponse> createThread(@RequestBody CreateThreadRequest request) {
        var createThreadCommand = ThreadAssembler.toCommandFromRequest(request);
        var threadId = this.threadCommandService.handle(createThreadCommand);

        if (Objects.isNull(threadId) || threadId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getThreadById = new GetThreadByIdQuery(threadId);
        var thread = this.threadQueryService.handle(getThreadById);

        if (thread.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var threadResponse = ThreadAssembler.toResponseFromEntity(thread.get());
        return new ResponseEntity<>(threadResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving all Threads.
     * @return ResponseEntity containing a list of ThreadResponse objects
     */
    @Operation(summary = "Get all Threads", description = "Retrieve a list of all Threads in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Threads retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ThreadResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Threads found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ThreadResponse>> getAllThreads() {
        var getAllThreadQuery = new GetAllThreadQuery();
        var threads = this.threadQueryService.handle(getAllThreadQuery);

        var threadResponses = threads.stream()
                .map(ThreadAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(threadResponses);
    }

    /**
     * Endpoint for retrieving a specific Thread by ID.
     * @param id ID of the Thread to be retrieved
     * @return ResponseEntity containing the ThreadResponse if found
     */
    @Operation(summary = "Get Thread by ID", description = "Retrieve a Thread by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thread retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ThreadResponse.class))),
            @ApiResponse(responseCode = "404", description = "Thread not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ThreadResponse> getThreadById(@PathVariable Long id) {
        var getThreadByIdQuery = new GetThreadByIdQuery(id);
        var thread = threadQueryService.handle(getThreadByIdQuery);

        if (thread.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var threadResponse = ThreadAssembler.toResponseFromEntity(thread.get());
        return ResponseEntity.ok(threadResponse);
    }

    /**
     * Endpoint for updating an existing Thread by ID.
     * @param id ID of the Thread to be updated
     * @param request Request object containing the updated details
     * @return ResponseEntity containing the updated ThreadResponse if successful
     */
    @Operation(summary = "Update Thread information", description = "Update the information of an existing Thread")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thread updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ThreadResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Thread not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ThreadResponse> updateThread(@PathVariable Long id, @RequestBody UpdateThreadRequest request) {
        var updateThreadCommand = ThreadAssembler.toCommandFromRequest(id, request);
        var updatedThread = this.threadCommandService.handle(updateThreadCommand);
        if (updatedThread.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var threadResponse = ThreadAssembler.toResponseFromEntity(updatedThread.get());
        return ResponseEntity.ok(threadResponse);
    }

    /**
     * Endpoint for deleting a Thread by ID.
     * @param id ID of the Thread to be deleted
     * @return ResponseEntity with no content if deleted successfully
     */
    @Operation(summary = "Delete Thread by ID", description = "Delete a Thread by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Thread deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Thread not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteThreadById(@PathVariable Long id) {
        var deleteThreadCommand = new DeleteThreadCommand(id);
        this.threadCommandService.handle(deleteThreadCommand);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for adding a message to a Thread.
     * @param threadId ID of the Thread
     * @param request Request object containing the message details
     * @return ResponseEntity containing the updated ThreadResponse
     */
    @Operation(summary = "Add a message to a Thread", description = "Add a new message to an existing Thread")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message added to Thread successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ThreadResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Thread not found", content = @Content)
    })
    @PostMapping("/{threadId}/messages")
    public ResponseEntity<ThreadResponse> addMessageToThread(@PathVariable Long threadId, @RequestBody AddMessageToThreadRequest request){
        var command = ThreadAssembler.toCommandFromRequest(threadId,request);
        this.threadCommandService.handle(command);

        var thread = this.threadQueryService.handle(new GetThreadByIdQuery(threadId));
        if(thread.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ThreadAssembler.toResponseFromEntity(thread.get()));
    }

    /**
     * Endpoint for retrieving Threads by Area Company ID.
     * @param areaCompanyId ID of the Area Company
     * @return ResponseEntity containing a list of ThreadResponse objects
     */
    @Operation(summary = "Get Threads by Area Company ID", description = "Retrieve a list of Threads associated with a specific Area Company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Threads retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ThreadResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Threads found for the specified Area Company ID", content = @Content)
    })
    @GetMapping("/area-company/{areaCompanyId}")
    public ResponseEntity<List<ThreadResponse>> getThreadByAreaCompany(@PathVariable Long areaCompanyId){
        var getThreadByAreaCompany = new GetThreadsByAreaCompanyIdQuery(areaCompanyId);
        var threads = this.threadQueryService.handle(getThreadByAreaCompany);
        if (threads.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var threadResponses = threads.stream()
                .map(ThreadAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(threadResponses);
    }
}
