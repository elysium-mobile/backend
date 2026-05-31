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
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.AreaCompanyResponse;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateAreaCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateAreaCompanyRequest;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/area-companies", produces = "application/json")
@Tag(name = "Company Areas", description = "Endpoints for managing Company Areas")
public class AreaCompanyController {

    private final AreaCompanyCommandService areaCompanyCommandService;
    private final AreaCompanyQueryService areaCompanyQueryService;

    public AreaCompanyController(AreaCompanyCommandService areaCompanyCommandService, AreaCompanyQueryService areaCompanyQueryService)
    {
        this.areaCompanyCommandService = areaCompanyCommandService;
        this.areaCompanyQueryService = areaCompanyQueryService;
    }

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

}
