package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllUnitOfWorkQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetUnitOfWorkByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.UnitOfWorkCommandService;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.UnitOfWorkQueryService;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers.UnitOfWorkAssembler;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateUnitOfWorkRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UnitOfWorkResponse;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateUnitOfWorkRequest;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/unit-of-work", produces = "application/json")
@Tag(name = "Unit of Work", description = "Endpoints for managing Unit of Work")
public class UnitOfWorkController {

    private final UnitOfWorkCommandService unitOfWorkCommandService;
    private final UnitOfWorkQueryService unitOfWorkQueryService;

    public UnitOfWorkController(UnitOfWorkCommandService unitOfWorkCommandService,UnitOfWorkQueryService unitOfWorkQueryService){
        this.unitOfWorkCommandService = unitOfWorkCommandService;
        this.unitOfWorkQueryService = unitOfWorkQueryService;
    }


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
}
