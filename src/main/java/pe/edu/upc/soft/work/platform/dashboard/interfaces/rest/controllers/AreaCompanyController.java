package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllAreaCompanyQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAreaCompanyByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.AreaCompanyCommandService;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.AreaCompanyQueryService;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers.AreaCompanyAssembler;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.AddUnitOfWorkToAreaCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.AreaCompanyResponse;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateAreaCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateAreaCompanyRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 *  Controller for managing Area Companies in the system. Provides endpoints for creating, retrieving, updating, and deleting Area Companies,
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/area-company", produces = "application/json")
@Tag(name = "Company Areas", description = "Endpoints for managing Company Areas")
public class AreaCompanyController {

    private final AreaCompanyCommandService areaCompanyCommandService;
    private final AreaCompanyQueryService areaCompanyQueryService;

    /**
     *  Constructor for AreaCompanyController. Initializes the command and query services for handling Area Company operations.
     * @param areaCompanyCommandService Service for handling commands related to Area Companies
     * @param areaCompanyQueryService   Service for handling queries related to Area Companies
     */
    public AreaCompanyController(AreaCompanyCommandService areaCompanyCommandService, AreaCompanyQueryService areaCompanyQueryService)
    {
        this.areaCompanyCommandService = areaCompanyCommandService;
        this.areaCompanyQueryService = areaCompanyQueryService;
    }

    /**
     *  Endpoint for creating a new Area Company. Accepts a CreateAreaCompanyRequest object in the request body and returns the created Area Company as a response.
     * @param request   Request object containing the details of the Area Company to be created
     * @return  ResponseEntity containing the created Area Company and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new Area Company", description = "Create a new Area Company in the system")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Area Company created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Area Company not found")
    })
    @PostMapping
    public ResponseEntity<AreaCompanyResponse> createAreaCompany(@RequestBody CreateAreaCompanyRequest request){
        var createAreaCompanyCommand= AreaCompanyAssembler.toCommandFromRequest(request);
        var areaCompanyId = this.areaCompanyCommandService.handle(createAreaCompanyCommand);

        if (areaCompanyId == null || areaCompanyId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getAreaCompanyById = new GetAreaCompanyByIdQuery(areaCompanyId);
        var areaCompany = this.areaCompanyQueryService.handle(getAreaCompanyById);

        if (areaCompany.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var areaCompanyResponse = AreaCompanyAssembler.toResponseFromEntity(areaCompany.get());
        return new ResponseEntity<>(areaCompanyResponse, HttpStatus.CREATED);
    }


    /**
     *  Endpoint for retrieving all Area Companies. Returns a list of AreaCompanyResponse objects representing all Area Companies in the system.
     * @return  ResponseEntity containing a list of AreaCompanyResponse objects and the appropriate HTTP status code
     */
    @Operation(summary = "Get all Area Companies", description = "Retrieve a list of all Area Companies in the system")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Area Companies retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No Area Companies found")
    }
    )
    @GetMapping
    public ResponseEntity<List<AreaCompanyResponse>> getAllAreaCompanies(){
        var areaCompanies = new GetAllAreaCompanyQuery();
        var areas = this.areaCompanyQueryService.handle(areaCompanies);

        var areaCompanyResponse = areas.stream()
                .map(AreaCompanyAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(areaCompanyResponse);
    }

    /**
     *  Endpoint for retrieving a specific Area Company by its ID. Accepts the ID of the Area Company as a path variable and returns the corresponding AreaCompanyResponse object if found.
     * @param id    ID of the Area Company to be retrieved
     * @return  ResponseEntity containing the AreaCompanyResponse object if found, or an appropriate HTTP status code if not found
     */
    @Operation(summary = "Get Area Company by ID", description = "Retrieve a specific Area Company by its ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Area Company retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Area Company not found")
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AreaCompanyResponse> getAreaCompanyById(@PathVariable Long id){
        var getAreaCompanyByIdQuery= new GetAreaCompanyByIdQuery(id);
        var areaCompany = this.areaCompanyQueryService.handle(getAreaCompanyByIdQuery);
        if (areaCompany.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var areaCompanyResponse = AreaCompanyAssembler.toResponseFromEntity(areaCompany.get());
        return ResponseEntity.ok(areaCompanyResponse);
    }

    /**
     *  Endpoint for updating an existing Area Company. Accepts the ID of the Area Company to be updated as a path variable and an UpdateAreaCompanyRequest object in the request body containing the updated details. Returns the updated AreaCompanyResponse object if the update is successful.
     * @param id    ID of the Area Company to be updated
     * @param request   Request object containing the updated details of the Area Company
     * @return  ResponseEntity containing the updated AreaCompanyResponse object if the update is successful, or an appropriate HTTP status code if the request is invalid or the Area Company is not found
     */
    @Operation(summary = "Update Area Company", description = "Update an existing Area Company by its ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Area Company updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Area Company not found")
    }
    )
    @PutMapping("/{id}")
    public ResponseEntity<AreaCompanyResponse> updateAreaCompany(@PathVariable Long id, @RequestBody UpdateAreaCompanyRequest request){
        var updateAreaCompanyCommand= AreaCompanyAssembler.toCommandFromRequest(id, request);
        var updateAreaCompany= this.areaCompanyCommandService.handle(updateAreaCompanyCommand);

        if (updateAreaCompany.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var areaCompanyResponse = AreaCompanyAssembler.toResponseFromEntity(updateAreaCompany.get());
        return ResponseEntity.ok(areaCompanyResponse);
    }

    /**
     *  Endpoint for deleting an existing Area Company by its ID. Accepts the ID of the Area Company to be deleted as a path variable and returns an appropriate HTTP status code based on the outcome of the delete operation.
     * @param id    ID of the Area Company to be deleted
     * @return  ResponseEntity with no content if the Area Company is deleted successfully, or an appropriate HTTP status code if the Area Company is not found
     */
    @Operation(summary = "Delete Area Company", description = "Delete an existing Area Company by its ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204", description = "Area Company deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Area Company not found")
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAreaCompanyById(@PathVariable Long id){
        var deleteAreaCompanyCommand= new DeleteAreaCompanyCommand(id);
        this.areaCompanyCommandService.handle(deleteAreaCompanyCommand);
        return ResponseEntity.noContent().build();
    }

    /**
     *  Endpoint for adding a Unit of Work to an existing Area Company. Accepts the ID of the Area Company as a path variable and an AddUnitOfWorkToAreaCompanyRequest object in the request body containing the details of the Unit of Work to be added. Returns the updated AreaCompanyResponse object if the Unit of Work is added successfully.
     * @param areaCompanyId  ID of the Area Company to which the Unit of Work will be added
     * @param request   Request object containing the details of the Unit of Work to be added to the Area Company
     * @return  ResponseEntity containing the updated AreaCompanyResponse object if the Unit of Work is added successfully, or an appropriate HTTP status code if the request is invalid or the Area Company or Unit of Work is not found
     */
    @Operation(
            summary = "Add Unit of Work to Area Company",
            description = "Add a Unit of Work to an existing Area Company by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unit of Work added to Area Company successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Area Company or Unit of Work not found")
    })
    @PostMapping("/{areaCompanyId}/unitsOfWork")
    public ResponseEntity<AreaCompanyResponse> addUnitOfWorkToAreaCompany(
            @PathVariable Long areaCompanyId,
            @RequestBody AddUnitOfWorkToAreaCompanyRequest request
            )
    {
        var command = AreaCompanyAssembler.toCommandFromRequest(areaCompanyId, request);
        this.areaCompanyCommandService.handle(command);

        var areaCompany = this.areaCompanyQueryService.handle(new GetAreaCompanyByIdQuery(areaCompanyId));
        if (areaCompany.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var areaCompanyResponse = AreaCompanyAssembler.toResponseFromEntity(areaCompany.get());
        return ResponseEntity.ok(areaCompanyResponse);
    }

}
