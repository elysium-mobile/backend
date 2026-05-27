package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllWorkTeamQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetWorkTeamByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.WorkTeamCommandService;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.WorkTeamQueryService;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers.WorkTeamAssembler;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateWorkTeamRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.WorkTeamResponse;

import java.util.List;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/work-teams", produces = "application/json")
@Tag(name = "Work Teams", description = "Endpoints for managing Work Teams")
public class WorkTeamController {

    private final WorkTeamCommandService workTeamCommandService;
    private final WorkTeamQueryService workTeamQueryService;

    public WorkTeamController(WorkTeamCommandService workTeamCommandService, WorkTeamQueryService workTeamQueryService){
        this.workTeamCommandService = workTeamCommandService;
        this.workTeamQueryService = workTeamQueryService;
    }

    @Operation(summary = "Create a new Work Team", description = "Create a new Work Team in the system")
    @ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Work Team created successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Work Team not found")
            }
    )
    @PostMapping
    public ResponseEntity<WorkTeamResponse> createWorkTeam(@RequestBody CreateWorkTeamRequest request){
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

}
