package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteWidgetCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllWidgetQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetWidgetByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.WidgetCommandService;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.WidgetQueryService;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers.WidgetAssembler;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateWidgetRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateWidgetRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.WidgetResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 *  Controller for managing Widgets in the system. Provides endpoints for creating, retrieving, updating, and deleting Widgets.
 */
@CrossOrigin(origins = "*", methods ={RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/widgets", produces = "application/json")
@Tag(name = "Widgets", description = "Endpoints for managing Widgets")
public class WidgetController {

    private final WidgetCommandService widgetCommandService;
    private final WidgetQueryService widgetQueryService;

    /**
     *  Constructor for WidgetController, initializes the command and query services.
     * @param widgetCommandService  Service for handling Widget commands (create, update, delete)
     * @param widgetQueryService    Service for handling Widget queries (retrieve)
     */
    public WidgetController(WidgetCommandService widgetCommandService, WidgetQueryService widgetQueryService)
    {
        this.widgetCommandService = widgetCommandService;
        this.widgetQueryService = widgetQueryService;
    }

    /**
     *  Endpoint for creating a new Widget. Accepts a CreateWidgetRequest and returns the created WidgetResponse.
     * @param request   Request body containing the details of the Widget to be created
     * @return  ResponseEntity containing the created WidgetResponse and appropriate HTTP status code
     */
    @Operation(summary = "Create a new Widget", description = "Create a new Widget in the system")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Widget created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Widget not found")
    })
    @PostMapping
    public ResponseEntity<WidgetResponse> createWidget(@RequestBody CreateWidgetRequest request)
    {
        var createWidgetCommand = WidgetAssembler.toCommandFromRequest(request);
        var widgetId = this.widgetCommandService.handle(createWidgetCommand);

        if (widgetId == null || widgetId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getWidgetById = new GetWidgetByIdQuery(widgetId);
        var widget = this.widgetQueryService.handle(getWidgetById);
        if (widget.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var widgetResponse = WidgetAssembler.toResponseFromEntity(widget.get());
        return new ResponseEntity<>(widgetResponse, HttpStatus.CREATED);
    }

    /**
     *  Endpoint for retrieving all Widgets in the system. Returns a list of WidgetResponse objects.
     * @return  ResponseEntity containing a list of WidgetResponse objects and appropriate HTTP status code
     */
    @Operation(summary = "Get all Widgets", description = "Retrieve a list of all Widgets in the system")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Widgets retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Widgets not found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<WidgetResponse>> getAllWidget(){
        var getAllWidgetQuery = new GetAllWidgetQuery();
        var widget = this.widgetQueryService.handle(getAllWidgetQuery);
        var widgetResponses = widget.stream()
                .map(WidgetAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(widgetResponses);
    }

    /**
     *  Endpoint for retrieving a Widget by its ID. Accepts a path variable for the Widget ID and returns the corresponding WidgetResponse.
     * @param id    Path variable representing the ID of the Widget to be retrieved
     * @return  ResponseEntity containing the WidgetResponse for the specified ID and appropriate HTTP status code
     */
    @Operation(summary = "Get Widget by ID", description = "Retrieve a Widget by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Widget retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Widget not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<WidgetResponse> getWidgetById(@PathVariable Long id){
        var getWidgetByIdQuery = new GetWidgetByIdQuery(id);
        var widget = this.widgetQueryService.handle(getWidgetByIdQuery);
        if (widget.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var widgetResponse = WidgetAssembler.toResponseFromEntity(widget.get());
        return ResponseEntity.ok(widgetResponse);
    }

    /**
     *  Endpoint for updating an existing Widget. Accepts a path variable for the Widget ID and a request body containing the updated details of the Widget. Returns the updated WidgetResponse.
     * @param id    Path variable representing the ID of the Widget to be updated
     * @param request   Request body containing the updated details of the Widget
     * @return  ResponseEntity containing the updated WidgetResponse and appropriate HTTP status code
     */
    @Operation(summary = "Update a Widget", description = "Update an existing Widget in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Widget updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Widget not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<WidgetResponse> updateWidget(@PathVariable Long id, @RequestBody UpdateWidgetRequest request){
        var updateWidgetCommand= WidgetAssembler.toCommandFromRequest(id, request);
        var updatedWidget = this.widgetCommandService.handle(updateWidgetCommand);

        if (updatedWidget.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var widgetResponse = WidgetAssembler.toResponseFromEntity(updatedWidget.get());
        return ResponseEntity.ok(widgetResponse);
    }

    /**
     *  Endpoint for deleting a Widget by its ID. Accepts a path variable for the Widget ID and deletes the corresponding Widget from the system. Returns an appropriate HTTP status code based on the outcome of the operation.
     * @param id    Path variable representing the ID of the Widget to be deleted
     * @return  ResponseEntity with appropriate HTTP status code indicating the result of the delete operation
     */
    @Operation(summary = "Delete a Widget", description = "Delete an existing Widget by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Widget deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Widget not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWidgetById(@PathVariable Long id){
        var deleteWidgetCommand = new DeleteWidgetCommand(id);
        this.widgetCommandService.handle(deleteWidgetCommand);
        return ResponseEntity.noContent().build();
    }
}
