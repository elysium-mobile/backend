package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteWorkTeamCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllWorkTeamQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetWorkTeamByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.WorkTeamCommandService;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.WorkTeamQueryService;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers.WorkTeamAssembler;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateWorkTeamRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateWorkTeamRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.WorkTeamResponse;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 *  Controller for managing Work Teams in the system. Provides endpoints for creating, retrieving, updating, and deleting Work Teams.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/work-teams", produces = "application/json")
@Tag(name = "Work Teams", description = "Endpoints for managing Work Teams")
public class WorkTeamController {

    private final WorkTeamCommandService workTeamCommandService;
    private final WorkTeamQueryService workTeamQueryService;

    /**
     *  Constructor for WorkTeamController, initializes the command and query services.
     * @param workTeamCommandService    Service for handling Work Team commands (create, update, delete)
     * @param workTeamQueryService  Service for handling Work Team queries (retrieve)
     */
    public WorkTeamController(WorkTeamCommandService workTeamCommandService, WorkTeamQueryService workTeamQueryService){
        this.workTeamCommandService = workTeamCommandService;
        this.workTeamQueryService = workTeamQueryService;
    }

    /**
     *  Endpoint for creating a new Work Team. Accepts a CreateWorkTeamRequest and returns the created WorkTeamResponse.
     * @param request   Request body containing the details of the Work Team to be created
     * @return  ResponseEntity containing the created WorkTeamResponse and appropriate HTTP status code
     */
    @Operation(summary = "Create a new Work Team", description = "Create a new Work Team in the system")
    @ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Work Team created successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Work Team not found")
            }
    )
    @PostMapping
    public ResponseEntity<WorkTeamResponse> createWorkTeam(@Valid @RequestBody CreateWorkTeamRequest request){
        var createWorkTeamCommand = WorkTeamAssembler.toCommandFromRequest(request);
        var workTeamId = this.workTeamCommandService.handle(createWorkTeamCommand);

        if (workTeamId == null || workTeamId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var getWorkTeamById = new GetWorkTeamByIdQuery(workTeamId);
        var workTeam = this.workTeamQueryService.handle(getWorkTeamById);

        if (workTeam.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var workTeamResponse = WorkTeamAssembler.toResponseFromEntity(workTeam.get());
        return new ResponseEntity<>(workTeamResponse, HttpStatus.CREATED);
    }

    /**
     *  Endpoint for retrieving all Work Teams in the system. Returns a list of WorkTeamResponse objects.
     * @return  ResponseEntity containing a list of WorkTeamResponse objects and appropriate HTTP status code
     */
    @Operation(summary = "Get All WorkTeams", description = "Retrieve a lis of all Work Team in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "WorkTeam retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Not found any Work Team")

    })
    @GetMapping
    public ResponseEntity<List<WorkTeamResponse>> getAllWorkTeam(){
        var getAllWorkTeamQuery= new GetAllWorkTeamQuery();
        var workTeam = this.workTeamQueryService.handle(getAllWorkTeamQuery);

        var workTeamResponse= workTeam.stream()
                .map(WorkTeamAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(workTeamResponse);
    }

    /**
     *  Endpoint for retrieving a Work Team by its ID. Accepts a path variable for the Work Team ID and returns the corresponding WorkTeamResponse.
     * @param id    Path variable representing the ID of the Work Team to be retrieved
     * @return  ResponseEntity containing the WorkTeamResponse for the specified ID and appropriate HTTP status code
     */
    @Operation(summary = "Get WorkTeam by ID", description = "Retrieve a WorkTeam by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "WorkTeam retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "WorkTeam not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<WorkTeamResponse> getWorkTeamById(@PathVariable Long id){
        var getWorkTeamByIdQuery = new GetWorkTeamByIdQuery(id);
        var workTeam= workTeamQueryService.handle(getWorkTeamByIdQuery);

        if (workTeam.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var workResponse = WorkTeamAssembler.toResponseFromEntity(workTeam.get());
       return ResponseEntity.ok(workResponse);
    }


    /**
     *  Endpoint for updating an existing Work Team. Accepts a path variable for the Work Team ID and an UpdateWorkTeamRequest containing the updated details. Returns the updated WorkTeamResponse.
     * @param id    Path variable representing the ID of the Work Team to be updated
     * @param request   Request body containing the updated details of the Work Team
     * @return  ResponseEntity containing the updated WorkTeamResponse and appropriate HTTP status code
     */
    @Operation(summary = "Update a WorkTeam", description = "Update an existing WorkTeam in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "WorkTeam updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "WorkTeam not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<WorkTeamResponse> updateWorkTeam(@PathVariable Long id, @Valid @RequestBody UpdateWorkTeamRequest request){
        var updateWorkTeamCommand = WorkTeamAssembler.toCommandFromRequest(id,request);
        var updatedWorkTeam= this.workTeamCommandService.handle(updateWorkTeamCommand);
        if (updatedWorkTeam.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var workTeamResponse = WorkTeamAssembler.toResponseFromEntity(updatedWorkTeam.get());
        return ResponseEntity.ok(workTeamResponse);
    }

    /**
     *  Endpoint for deleting a Work Team by its ID. Accepts a path variable for the Work Team ID and deletes the corresponding Work Team. Returns a 204 No Content status if the deletion is successful.
     */
    @Operation(summary = "Delete a WorkTeam", description = "Delete an existing WorkTeam by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "WorkTeam deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "WorkTeam not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkTeamById(@PathVariable Long id){
        var deleteWorkTeamCommand= new DeleteWorkTeamCommand(id);
        this.workTeamCommandService.handle(deleteWorkTeamCommand);
        return ResponseEntity.noContent().build();
    }
}
