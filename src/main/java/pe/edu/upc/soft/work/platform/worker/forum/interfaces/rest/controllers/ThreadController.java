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

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/threads", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Threads", description = "Endpoints for managing Threads")
public class ThreadController {

    private final ThreadCommandService threadCommandService;
    private final ThreadQueryService threadQueryService;

    public ThreadController(ThreadCommandService threadCommandService, ThreadQueryService threadQueryService) {
        this.threadCommandService = threadCommandService;
        this.threadQueryService = threadQueryService;
    }

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


    @Operation(summary = "Add a message to a Thread", description = "Add a new message to an existing Thread")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message added to Thread successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ThreadResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Thread not found", content = @Content)
    })
    @PostMapping("/{id}/messages")
    public ResponseEntity<ThreadResponse> addMessageToThread(@PathVariable Long id, @RequestBody AddMessageToThreadRequest request){
        var command = ThreadAssembler.toCommandFromRequest(id,request);
        this.threadCommandService.handle(command);

        var thread = this.threadQueryService.handle(new GetThreadByIdQuery(id));
        if(thread.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ThreadAssembler.toResponseFromEntity(thread.get()));
    }
}
