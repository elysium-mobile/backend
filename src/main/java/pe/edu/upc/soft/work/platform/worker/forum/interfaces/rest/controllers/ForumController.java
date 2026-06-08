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
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllForumQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetForumByIdQuery;
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

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/forums", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Forums", description = "Endpoints for managing Forums")
public class ForumController {

    private final ForumCommandService forumCommandService;
    private final ForumQueryService forumQueryService;

    public ForumController(ForumCommandService forumCommandService, ForumQueryService forumQueryService) {
        this.forumCommandService = forumCommandService;
        this.forumQueryService = forumQueryService;
    }

    @Operation(summary = "Create a new Forum", description = "Create a new Forum in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Forum created successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                            schema = @Schema(implementation = ForumResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Forum not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ForumResponse> createForum(@RequestBody CreateForumRequest request) {
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

    @Operation(summary = "Update Forum information", description = "Update the information of an existing Forum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forum updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ForumResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Forum not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ForumResponse> updateForum(@PathVariable Long id, @RequestBody UpdateForumRequest request) {
        var updateForumCommand = ForumAssembler.toCommandFromRequest(id, request);
        var updatedForum = this.forumCommandService.handle(updateForumCommand);
        if (updatedForum.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var forumResponse = ForumAssembler.toResponseFromEntity(updatedForum.get());
        return ResponseEntity.ok(forumResponse);
    }

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

    @Operation(summary = "Add Category to Forum", description = "Add a Category to an existing Forum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category added to Forum successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ForumResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Forum or Category not found", content = @Content)
    })
    @PostMapping("/{id}/categories")
    public ResponseEntity<ForumResponse> addCategoryToForum(@PathVariable Long id, @RequestBody AddCategoryToForumRequest request){
        var command = ForumAssembler.toCommandFromRequest(id, request);
        this.forumCommandService.handle(command);
        var forum = this.forumQueryService.handle(new GetForumByIdQuery(id));
        if (forum.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var forumResponse = ForumAssembler.toResponseFromEntity(forum.get());
        return ResponseEntity.ok(forumResponse);
    }
}
