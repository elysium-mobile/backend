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
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.DeletePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetPerformanceByIdQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetAllPerformanceQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.services.PerformanceCommandService;
import pe.edu.upc.soft.work.platform.profile.performance.domain.services.PerformanceQueryService;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.assemblers.PerformanceAssembler;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.AddCommentEmployeeToPerformanceRequest;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.CreatePerformanceRequest;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.UpdatePerformanceRequest;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.PerformanceResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/performances", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Performances", description = "Endpoints for managing Performances")
public class PerformanceController {

    private final PerformanceCommandService performanceCommandService;
    private final PerformanceQueryService performanceQueryService;

    public PerformanceController(PerformanceCommandService performanceCommandService, PerformanceQueryService performanceQueryService) {
        this.performanceCommandService = performanceCommandService;
        this.performanceQueryService = performanceQueryService;
    }

    @Operation(summary = "Create a new Performance", description = "Create a new Performance in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Performance created successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                            schema = @Schema(implementation = PerformanceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Performance not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PerformanceResponse> createPerformance(@RequestBody CreatePerformanceRequest request) {
        var createPerformanceCommand = PerformanceAssembler.toCommandFromRequest(request);
        var performanceId = this.performanceCommandService.handle(createPerformanceCommand);

        if (Objects.isNull(performanceId) || performanceId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getPerformanceById = new GetPerformanceByIdQuery(performanceId);
        var performance = this.performanceQueryService.handle(getPerformanceById);

        if (performance.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var performanceResponse = PerformanceAssembler.toResponseFromEntity(performance.get());
        return new ResponseEntity<>(performanceResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all Performances", description = "Retrieve a list of all Performances in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Performances retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PerformanceResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Performances found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PerformanceResponse>> getAllPerformances() {
        var getAllPerformanceQuery = new GetAllPerformanceQuery();
        var performances = this.performanceQueryService.handle(getAllPerformanceQuery);

        var performanceResponses = performances.stream()
                .map(PerformanceAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(performanceResponses);
    }

    @Operation(summary = "Get Performance by ID", description = "Retrieve a Performance by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Performance retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PerformanceResponse.class))),
            @ApiResponse(responseCode = "404", description = "Performance not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PerformanceResponse> getPerformanceById(@PathVariable Long id) {
        var getPerformanceByIdQuery = new GetPerformanceByIdQuery(id);
        var performance = performanceQueryService.handle(getPerformanceByIdQuery);

        if (performance.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var performanceResponse = PerformanceAssembler.toResponseFromEntity(performance.get());
        return ResponseEntity.ok(performanceResponse);
    }

    @Operation(summary = "Update Performance information", description = "Update the information of an existing Performance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Performance updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PerformanceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Performance not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PerformanceResponse> updatePerformance(@PathVariable Long id, @RequestBody UpdatePerformanceRequest request) {
        var updatePerformanceCommand = PerformanceAssembler.toCommandFromRequest(id, request);
        var updatedPerformance = this.performanceCommandService.handle(updatePerformanceCommand);
        if (updatedPerformance.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var performanceResponse = PerformanceAssembler.toResponseFromEntity(updatedPerformance.get());
        return ResponseEntity.ok(performanceResponse);
    }

    @Operation(summary = "Delete Performance by ID", description = "Delete a Performance by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Performance deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Performance not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerformanceById(@PathVariable Long id) {
        var deletePerformanceCommand = new DeletePerformanceCommand(id);
        this.performanceCommandService.handle(deletePerformanceCommand);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add CommentEmployee to Performance", description = "Add a CommentEmployee to an existing Performance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CommentEmployee added to Performance successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PerformanceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Performance or CommentEmployee not found", content = @Content)
    })
    @PostMapping("/{id}/comment-employee")
    public ResponseEntity<PerformanceResponse> addCommentEmployeeToPerformance(@PathVariable Long id, @RequestBody AddCommentEmployeeToPerformanceRequest request){
        var command = PerformanceAssembler.toCommandFromRequest(id, request);
        this.performanceCommandService.handle(command);

        var performance = this.performanceQueryService.handle(new GetPerformanceByIdQuery(id));
        if (performance.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(PerformanceAssembler.toResponseFromEntity(performance.get()));
    }

    @Operation(summary = "Get Performance by Employee ID", description = "Retrieve a Performance by the unique identifier of the associated Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Performance retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PerformanceResponse.class))),
            @ApiResponse(responseCode = "404", description = "Performance not found for the given Employee ID", content = @Content)
    })
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<PerformanceResponse> getPerformanceByEmployeeId(@PathVariable Long employeeId){
        var getPerformanceByEmployeeIdQuery = new GetPerformanceByIdQuery(employeeId);
        var performance = performanceQueryService.handle(getPerformanceByEmployeeIdQuery);

        if (performance.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var performanceResponse = PerformanceAssembler.toResponseFromEntity(performance.get());
        return ResponseEntity.ok(performanceResponse);
    }
}
