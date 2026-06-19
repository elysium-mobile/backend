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
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllUnitOfWorkQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetUnitOfWorkByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.UnitOfWorkCommandService;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.UnitOfWorkQueryService;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers.UnitOfWorkAssembler;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.AddWorkTeamToUnitOFWorkRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateUnitOfWorkRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UnitOfWorkResponse;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateUnitOfWorkRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 *  Controller for managing Units of Work in the system. Provides endpoints for creating, retrieving, updating, and deleting Units of Work.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/unit-of-work", produces = "application/json")
@Tag(name = "Unit of Work", description = "Endpoints for managing Unit of Work")
public class UnitOfWorkController {


    private final UnitOfWorkCommandService unitOfWorkCommandService;
    private final UnitOfWorkQueryService unitOfWorkQueryService;

    /**
     *  Constructor for UnitOfWorkController.
     * @param unitOfWorkCommandService  Service for handling commands related to Unit of Work.
     * @param unitOfWorkQueryService    Service for handling queries related to Unit of Work.
     */
    public UnitOfWorkController(UnitOfWorkCommandService unitOfWorkCommandService,UnitOfWorkQueryService unitOfWorkQueryService){
        this.unitOfWorkCommandService = unitOfWorkCommandService;
        this.unitOfWorkQueryService = unitOfWorkQueryService;
    }


    /**
     *  Endpoint for creating a new Unit of Work. Accepts a CreateUnitOfWorkRequest and returns the created Unit of Work as a UnitOfWorkResponse.
     * @param request   Request body containing the details of the Unit of Work to be created.
     * @return  ResponseEntity containing the created Unit of Work or an appropriate error response.
     */
    @Operation(summary = "Create a new Unit of Work", description = "Create a new Unit of Work in the system")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Unit of Work created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Unit of Work not found")
            }
    )
    @PostMapping
    public ResponseEntity<UnitOfWorkResponse> createUnitOfWork(@RequestBody CreateUnitOfWorkRequest request){
        var createUnitOfWorkCommand = UnitOfWorkAssembler.toCommandFromRequest(request);
        var unitOfWorkId = this.unitOfWorkCommandService.handle(createUnitOfWorkCommand);

        if (unitOfWorkId == null || unitOfWorkId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getUnitOfWorkById = new GetUnitOfWorkByIdQuery(unitOfWorkId);
        var unitOfWork = this.unitOfWorkQueryService.handle(getUnitOfWorkById);

        if (unitOfWork.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var unitOfWorkResponse = UnitOfWorkAssembler.toResponseFromEntity(unitOfWork.get());
        return new ResponseEntity<>(unitOfWorkResponse, HttpStatus.CREATED);
    }

    /**
     *  Endpoint for retrieving all Units of Work in the system. Returns a list of UnitOfWorkResponse objects representing each Unit of Work.
     * @return  ResponseEntity containing a list of all Units of Work or an appropriate error response.
     */
    @Operation(summary = "Get all Units of Work", description = "Retrieve a list of all Units of Work in the system")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Units of Work retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Units of Work not found")
            }
    )
    @GetMapping
    public ResponseEntity<List<UnitOfWorkResponse>> getAllUnitOfWork(){
        var getAllUnitOfWork = new GetAllUnitOfWorkQuery();
        var unitOfWorkList = this.unitOfWorkQueryService.handle(getAllUnitOfWork);
        var unitOfWorkResponseList = unitOfWorkList.stream()
                .map(UnitOfWorkAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(unitOfWorkResponseList);
    }

    /**
     *  Endpoint for retrieving a specific Unit of Work by its ID. Returns a UnitOfWorkResponse representing the requested Unit of Work.
     * @param id    ID of the Unit of Work to be retrieved.
     * @return  ResponseEntity containing the requested Unit of Work or an appropriate error response if not found.
     */
    @Operation(summary = "Get a Unit of Work by ID", description = "Retrieve a specific Unit of Work by its ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Unit of Work retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Unit of Work not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UnitOfWorkResponse> getUnitOfWork(@PathVariable Long id){
        var getUnitOfWorkById = new GetUnitOfWorkByIdQuery(id);
        var unitOfWork = this.unitOfWorkQueryService.handle(getUnitOfWorkById);

        if (unitOfWork.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var unitOfWorkResponse = UnitOfWorkAssembler.toResponseFromEntity(unitOfWork.get());
        return ResponseEntity.ok(unitOfWorkResponse);
    }


    /**
     *  Endpoint for updating an existing Unit of Work. Accepts an UpdateUnitOfWorkRequest containing the updated details and returns the updated Unit of Work as a UnitOfWorkResponse.
     * @param id    ID of the Unit of Work to be updated.
     * @param request   Request body containing the updated details of the Unit of Work.
     * @return  ResponseEntity containing the updated Unit of Work or an appropriate error response if the update fails or the Unit of Work is not found.
     */
    @Operation(summary = "Update a Unit of Work", description = "Update an existing Unit of Work by its ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Unit of Work updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Unit of Work not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UnitOfWorkResponse> updateUnitOfWork(@PathVariable Long id, @RequestBody UpdateUnitOfWorkRequest request){
        var updateUnitOfWorkCommand= UnitOfWorkAssembler.toCommandFromRequest(id, request);
        var updatedUnit = this.unitOfWorkCommandService.handle(updateUnitOfWorkCommand);
        if (updatedUnit.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var unitResponse = UnitOfWorkAssembler.toResponseFromEntity(updatedUnit.get());
        return ResponseEntity.ok(unitResponse);
    }

    /**
     *  Endpoint for deleting an existing Unit of Work by its ID. Returns a 204 No Content response if the deletion is successful or a 404 Not Found response if the Unit of Work does not exist.
     * @param id    ID of the Unit of Work to be deleted.
     * @return  ResponseEntity indicating the result of the delete operation.
     */
    @Operation(summary = "Delete a Unit of Work", description = "Delete an existing Unit of Work by its ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Unit of Work deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Unit of Work not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUnitOfWorkById(@PathVariable Long id){
        var deleteUnitOfWorkCommand= new DeleteUnitOfWorkCommand(id);
        this.unitOfWorkCommandService.handle(deleteUnitOfWorkCommand);
        return ResponseEntity.noContent().build();
    }

    /**
     *  Endpoint for adding a WorkTeam to an existing Unit of Work. Accepts an AddWorkTeamToUnitOFWorkRequest containing the ID of the WorkTeam to be added and returns the updated Unit of Work as a UnitOfWorkResponse.
     * @param uniOfWorkId   ID of the Unit of Work to which the WorkTeam will be added.
     * @param request   Request body containing the ID of the WorkTeam to be added to the Unit of Work.
     * @return  ResponseEntity containing the updated Unit of Work with the added WorkTeam or an appropriate error response if the operation fails, the Unit of Work is not found, or the WorkTeam is not found.
     */
    @Operation(
            summary = "Add a WorkTeam to a Unit of Work",
            description = "Links an existing WorkTeam to the given Unit of Work.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "WorkTeam added successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UnitOfWorkResponse.class))),
            @ApiResponse(responseCode = "400", description = "WorkTeam already belongs to this unit or invalid data", content = @Content),
            @ApiResponse(responseCode = "404", description = "UnitOfWork or WorkTeam not found", content = @Content)
    })
    @PostMapping("/{uniOfWorkId}/work-teams")
    public ResponseEntity<UnitOfWorkResponse> addWorkTeamToUnitOfWork(
            @PathVariable Long uniOfWorkId,
            @RequestBody AddWorkTeamToUnitOFWorkRequest request) {

        var command = UnitOfWorkAssembler.toCommandFromRequest(uniOfWorkId, request);
        this.unitOfWorkCommandService.handle(command);

        var unitOfWork = this.unitOfWorkQueryService.handle(new GetUnitOfWorkByIdQuery(uniOfWorkId));
        if (unitOfWork.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UnitOfWorkAssembler.toResponseFromEntity(unitOfWork.get()));
    }
}
