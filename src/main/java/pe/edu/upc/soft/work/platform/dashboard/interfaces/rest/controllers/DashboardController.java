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

/**
 *  Controller for managing Dashboards in the system. It provides endpoints for creating, retrieving, updating, and deleting Dashboards,
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/dashboards", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Dashboards", description = "Endpoints for managing Dashboards")
public class DashboardController {

    private final DashboardCommandService dashboardCommandService;
    private final DashboardQueryService dashboardQueryService;

    /**
     *  Constructor for DashboardController.
     * @param dashboardCommandService   Service for handling commands related to Dashboards.
     * @param dashboardQueryService  Service for handling queries related to Dashboards.
     */
    public DashboardController(DashboardCommandService dashboardCommandService, DashboardQueryService dashboardQueryService) {
        this.dashboardCommandService = dashboardCommandService;
        this.dashboardQueryService = dashboardQueryService;
    }

    /**
     *  Endpoint for creating a new Dashboard. It accepts a CreateDashboardRequest object in the request body and returns a DashboardResponse object if the creation is successful.
     * @param request   Request body containing the information needed to create a new Dashboard.
     * @return  ResponseEntity containing the created DashboardResponse object and an HTTP status code indicating the result of the operation.
     */
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

    /**
     *  Endpoint for retrieving all Dashboards in the system. It returns a list of DashboardResponse objects if there are any Dashboards available.
     * @return  ResponseEntity containing a list of DashboardResponse objects and an HTTP status code indicating the result of the operation.
     */
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

    /**
     *  Endpoint for retrieving a specific Dashboard by its unique identifier. It accepts the Dashboard ID as a path variable and returns a DashboardResponse object if the Dashboard is found.
     * @param id    Path variable containing the unique identifier of the Dashboard to be retrieved.
     * @return  ResponseEntity containing the DashboardResponse object and an HTTP status code indicating the result of the operation.
     */
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

    /**
     *  Endpoint for updating an existing Dashboard. It accepts the Dashboard ID as a path variable and an UpdateDashboardRequest object in the request body. If the update is successful, it returns a DashboardResponse object with the updated information.
     * @param id    Path variable containing the unique identifier of the Dashboard to be updated.
     * @param request   Request body containing the information needed to update the existing Dashboard.
     * @return  ResponseEntity containing the updated DashboardResponse object and an HTTP status code indicating the result of the operation.
     */
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

    /**
     *  Endpoint for deleting an existing Dashboard by its unique identifier.
     *  It accepts the Dashboard ID as a path variable and returns an HTTP status code indicating the result of the operation. If the deletion is successful, it returns a 204 No Content status code. If the Dashboard is not found, it returns a 404 Not Found status code.
     * @param id    Path variable containing the unique identifier of the Dashboard to be deleted.
     * @return  ResponseEntity with an HTTP status code indicating the result of the operation.
     */
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

    /**
     *  Endpoint for adding an existing Widget to a Dashboard.
     *  It accepts the Dashboard ID as a path variable and an AddWidgetToDashboardRequest object in the request body. If the operation is successful, it returns a DashboardResponse object with the updated information of the Dashboard, including the newly added Widget.
     * @param dashboardId   Path variable containing the unique identifier of the Dashboard to which the Widget will be added.
     * @param request   Request body containing the information needed to link the existing Widget to the given Dashboard.
     * @return
     */
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
    @PostMapping("/{dashboardId}/widgets")
    public ResponseEntity<DashboardResponse> addWidgetToDashboard(
            @PathVariable Long dashboardId,
            @RequestBody AddWidgetToDashboardRequest request) {

        var command = DashboardAssembler.toCommandFromRequest(dashboardId, request);
        this.dashboardCommandService.handle(command);

        var dashboard = this.dashboardQueryService.handle(new GetDashboardByIdQuery(dashboardId));
        if (dashboard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(DashboardAssembler.toResponseFromEntity(dashboard.get()));
    }


    /**
     *  Endpoint for retrieving a list of Dashboards associated with a specific Company ID. It accepts the Company ID as a path variable and returns a list of DashboardResponse objects if there are any Dashboards found for the given Company ID. If no Dashboards are found, it returns a 404 Not Found status code.
     * @param companyId  Path variable containing the unique identifier of the Company for which the Dashboards will be retrieved.
     * @return  ResponseEntity containing a list of DashboardResponse objects and an HTTP status code indicating the result of the operation.
     */
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
