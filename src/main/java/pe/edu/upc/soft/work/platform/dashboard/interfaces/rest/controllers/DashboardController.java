package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.controllers;

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
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetDashboardByCompanyIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetDashboardByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllDashboardQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.DashboardCommandService;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.DashboardQueryService;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers.DashboardAssembler;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.AddWidgetToDashboardRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateDashboardRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateDashboardRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.DashboardResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/dashboards", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Dashboards", description = "Endpoints for managing Dashboards")
public class DashboardController {

    private final DashboardCommandService dashboardCommandService;
    private final DashboardQueryService dashboardQueryService;

    public DashboardController(DashboardCommandService dashboardCommandService, DashboardQueryService dashboardQueryService) {
        this.dashboardCommandService = dashboardCommandService;
        this.dashboardQueryService = dashboardQueryService;
    }

    @Operation(summary = "Create a new Dashboard", description = "Create a new Dashboard in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dashboard created successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                            schema = @Schema(implementation = DashboardResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Dashboard not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<DashboardResponse> createDashboard(@RequestBody CreateDashboardRequest request) {
        var createDashboardCommand = DashboardAssembler.toCommandFromRequest(request);
        var dashboardId = this.dashboardCommandService.handle(createDashboardCommand);

        if (Objects.isNull(dashboardId) || dashboardId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getDashboardById = new GetDashboardByIdQuery(dashboardId);
        var dashboard = this.dashboardQueryService.handle(getDashboardById);

        if (dashboard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var dashboardResponse = DashboardAssembler.toResponseFromEntity(dashboard.get());
        return new ResponseEntity<>(dashboardResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all Dashboards", description = "Retrieve a list of all Dashboards in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboards retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DashboardResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Dashboards found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<DashboardResponse>> getAllDashboards() {
        var getAllDashboardQuery = new GetAllDashboardQuery();
        var dashboards = this.dashboardQueryService.handle(getAllDashboardQuery);

        var dashboardResponses = dashboards.stream()
                .map(DashboardAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dashboardResponses);
    }

    @Operation(summary = "Get Dashboard by ID", description = "Retrieve a Dashboard by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DashboardResponse.class))),
            @ApiResponse(responseCode = "404", description = "Dashboard not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<DashboardResponse> getDashboardById(@PathVariable Long id) {
        var getDashboardByIdQuery = new GetDashboardByIdQuery(id);
        var dashboard = dashboardQueryService.handle(getDashboardByIdQuery);

        if (dashboard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var dashboardResponse = DashboardAssembler.toResponseFromEntity(dashboard.get());
        return ResponseEntity.ok(dashboardResponse);
    }

    @Operation(summary = "Update Dashboard information", description = "Update the information of an existing Dashboard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DashboardResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Dashboard not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<DashboardResponse> updateDashboard(@PathVariable Long id, @RequestBody UpdateDashboardRequest request) {
        var updateDashboardCommand = DashboardAssembler.toCommandFromRequest(id, request);
        var updatedDashboard = this.dashboardCommandService.handle(updateDashboardCommand);
        if (updatedDashboard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var dashboardResponse = DashboardAssembler.toResponseFromEntity(updatedDashboard.get());
        return ResponseEntity.ok(dashboardResponse);
    }

    @Operation(summary = "Delete Dashboard by ID", description = "Delete a Dashboard by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dashboard deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Dashboard not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDashboardById(@PathVariable Long id) {
        var deleteDashboardCommand = new DeleteDashboardCommand(id);
        this.dashboardCommandService.handle(deleteDashboardCommand);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Add a Widget to a Dashboard",
            description = "Links an existing Widget to the given Dashboard.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Widget added successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DashboardResponse.class))),
            @ApiResponse(responseCode = "400", description = "Widget already belongs to this dashboard or invalid data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Dashboard or Widget not found", content = @Content)
    })
    @PostMapping("/{id}/widgets")
    public ResponseEntity<DashboardResponse> addWidgetToDashboard(
            @PathVariable Long id,
            @RequestBody AddWidgetToDashboardRequest request) {

        var command = DashboardAssembler.toCommandFromRequest(id, request);
        this.dashboardCommandService.handle(command);

        var dashboard = this.dashboardQueryService.handle(new GetDashboardByIdQuery(id));
        if (dashboard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(DashboardAssembler.toResponseFromEntity(dashboard.get()));
    }


    @Operation(summary = "Get Dashboards by Company ID", description = "Retrieve a list of Dashboards associated with a specific Company ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboards retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DashboardResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Dashboards found for the given Company ID", content = @Content)
    })
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<DashboardResponse>> getByCompanyId(@PathVariable Long companyId){
        var query = new GetDashboardByCompanyIdQuery(companyId);
        var dashboards = this.dashboardQueryService.handle(query);

        if (dashboards.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var dashboardResponse = dashboards.stream()
                .map(DashboardAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dashboardResponse);
    }
}
