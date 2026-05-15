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
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetCompanyByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllCompanyQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.CompanyCommandService;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.CompanyQueryService;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers.CompanyAssembler;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CompanyResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/companies", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Companies", description = "Endpoints for managing Companies")
public class CompanyController {

    private final CompanyCommandService companyCommandService;
    private final CompanyQueryService companyQueryService;

    public CompanyController(CompanyCommandService companyCommandService, CompanyQueryService companyQueryService) {
        this.companyCommandService = companyCommandService;
        this.companyQueryService = companyQueryService;
    }

    @Operation(summary = "Create a new Company", description = "Create a new Company in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Company created successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                            schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Company not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@RequestBody CreateCompanyRequest request) {
        var createCompanyCommand = CompanyAssembler.toCommandFromRequest(request);
        var companyId = this.companyCommandService.handle(createCompanyCommand);

        if (Objects.isNull(companyId) || companyId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getCompanyById = new GetCompanyByIdQuery(companyId);
        var company = this.companyQueryService.handle(getCompanyById);

        if (company.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var companyResponse = CompanyAssembler.toResponseFromEntity(company.get());
        return new ResponseEntity<>(companyResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all Companys", description = "Retrieve a list of all Companys in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Companys retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Companys found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanys() {
        var getAllCompanyQuery = new GetAllCompanyQuery();
        var companys = this.companyQueryService.handle(getAllCompanyQuery);

        var companyResponses = companys.stream()
                .map(CompanyAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(companyResponses);
    }

    @Operation(summary = "Get Company by ID", description = "Retrieve a Company by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "404", description = "Company not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        var getCompanyByIdQuery = new GetCompanyByIdQuery(id);
        var company = companyQueryService.handle(getCompanyByIdQuery);

        if (company.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var companyResponse = CompanyAssembler.toResponseFromEntity(company.get());
        return ResponseEntity.ok(companyResponse);
    }

    @Operation(summary = "Update Company information", description = "Update the information of an existing Company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Company not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long id, @RequestBody UpdateCompanyRequest request) {
        var updateCompanyCommand = CompanyAssembler.toCommandFromRequest(id, request);
        var updatedCompany = this.companyCommandService.handle(updateCompanyCommand);
        if (updatedCompany.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var companyResponse = CompanyAssembler.toResponseFromEntity(updatedCompany.get());
        return ResponseEntity.ok(companyResponse);
    }

    @Operation(summary = "Delete Company by ID", description = "Delete a Company by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Company deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Company not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompanyById(@PathVariable Long id) {
        var deleteCompanyCommand = new DeleteCompanyCommand(id);
        this.companyCommandService.handle(deleteCompanyCommand);
        return ResponseEntity.noContent().build();
    }
}
