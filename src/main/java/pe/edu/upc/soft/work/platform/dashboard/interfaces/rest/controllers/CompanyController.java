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
import jakarta.validation.Valid;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetCompaniesByNameQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetCompanyByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllCompanyQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.CompanyCommandService;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.CompanyQueryService;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers.CompanyAssembler;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *  Controller for managing Company entities. Provides endpoints for creating, retrieving, updating, and deleting Companies,
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/companies", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Company", description = "Endpoints for managing Companies")
public class CompanyController {

    private final CompanyCommandService companyCommandService;
    private final CompanyQueryService companyQueryService;

    /**
     *  Constructor for CompanyController.
     * @param companyCommandService Service for handling commands related to Company entities.
     * @param companyQueryService   Service for handling queries related to Company entities.
     */
    public CompanyController(CompanyCommandService companyCommandService, CompanyQueryService companyQueryService) {
        this.companyCommandService = companyCommandService;
        this.companyQueryService = companyQueryService;
    }

    /**
     *  Endpoint for creating a new Company. Accepts a CreateCompanyRequest and returns the created Company as a CompanyResponse.
     * @param request   Request body containing the details of the Company to be created.
     * @return  ResponseEntity containing the created CompanyResponse and appropriate HTTP status code.
     */
    @Operation(summary = "Create a new Company", description = "Create a new Company in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Company created successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                            schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Company not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CreateCompanyRequest request) {
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

    /**
     *  Endpoint for retrieving all Companies. Returns a list of CompanyResponse objects representing all Companies in the system.
     * @return  ResponseEntity containing a list of CompanyResponse objects and appropriate HTTP status code.
     */
    @Operation(summary = "Get all Companies", description = "Retrieve a list of all Companies in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Companies retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Companies found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        var getAllCompanyQuery = new GetAllCompanyQuery();
        var company = this.companyQueryService.handle(getAllCompanyQuery);

        var companyResponses = company.stream()
                .map(CompanyAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(companyResponses);
    }

    /**
     *  Endpoint for retrieving a Company by its ID. Accepts a Company ID as a path variable and returns the corresponding CompanyResponse if found.
     * @param id    ID of the Company to be retrieved.
     * @return  ResponseEntity containing the CompanyResponse if found, or appropriate HTTP status code if not found or if the request is invalid.
     */
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

    /**
     *  Endpoint for updating an existing Company. Accepts a Company ID as a path variable and an UpdateCompanyRequest in the request body, and returns the updated CompanyResponse if the update is successful.
     * @param id    ID of the Company to be updated.
     * @param request   Request body containing the updated details of the Company.
     * @return  ResponseEntity containing the updated CompanyResponse if the update is successful, or appropriate HTTP status code if the request is invalid or if the Company is not found.
     */
    @Operation(summary = "Update Company information", description = "Update the information of an existing Company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Company not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long id, @Valid @RequestBody UpdateCompanyRequest request) {
        var updateCompanyCommand = CompanyAssembler.toCommandFromRequest(id, request);
        var updatedCompany = this.companyCommandService.handle(updateCompanyCommand);
        if (updatedCompany.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var companyResponse = CompanyAssembler.toResponseFromEntity(updatedCompany.get());
        return ResponseEntity.ok(companyResponse);
    }

    /**
     *  Endpoint for deleting a Company by its ID. Accepts a Company ID as a path variable and deletes the corresponding Company if found.
     * @param id    ID of the Company to be deleted.
     * @return  ResponseEntity with no content if the deletion is successful, or appropriate HTTP status code if the Company is not found.
     */
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

    /**
     *  Endpoint for adding an employee to a Company. Accepts a Company ID as a path variable and an AddEmployeeToCompanyRequest in the request body, and returns the updated CompanyResponse if the employee is added successfully.
     * @param companyId ID of the Company to which the employee will be added.
     * @param request   Request body containing the details of the employee to be added to the Company.
     * @return  ResponseEntity containing the updated CompanyResponse if the employee is added successfully, or appropriate HTTP status code if the request is invalid, if the employee already belongs to the company, or if the Company or Employee is not found.
     */
    @Operation(
            summary = "Add an employee to a Company",
            description = "Links an existing UserAccount (employee) to the given Company. " +
                    "The employee must already exist in the IAM context.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee added successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Employee already belongs to this company or invalid data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Company or Employee not found", content = @Content)
    })
    @PostMapping("/{companyId}/employees")
    public ResponseEntity<CompanyResponse> addEmployeeToCompany(@PathVariable Long companyId, @Valid @RequestBody AddEmployeeToCompanyRequest request){
        var command = CompanyAssembler.toCommandFromRequest(companyId, request);
        this.companyCommandService.handle(command);

        var company = this.companyQueryService.handle(new GetCompanyByIdQuery(companyId));
        if (company.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(CompanyAssembler.toResponseFromEntity(company.get()));
    }

    /**
     *  Endpoint for adding an AreaCompany to a Company. Accepts a Company ID as a path variable and an AddAreaCompanyToCompanyRequest in the request body, and returns the updated CompanyResponse if the AreaCompany is added successfully.
     * @param companyId ID of the Company to which the AreaCompany will be added.
     * @param request   Request body containing the details of the AreaCompany to be added to the Company.
     * @return  ResponseEntity containing the updated CompanyResponse if the AreaCompany is added successfully, or appropriate HTTP status code if the request is invalid, if the AreaCompany already belongs to the company, or if the Company or AreaCompany is not found.
     */
    @Operation(
            summary = "Add an AreaCompany to a Company",
            description = "Links an existing AreaCompany to the given Company.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AreaCompany added successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "400", description = "AreaCompany already belongs to this company or invalid data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Company or AreaCompany not found", content = @Content)
    })
    @PostMapping("/{companyId}/area-companies")
    public ResponseEntity<CompanyResponse> addAreaCompanyToCompany(@PathVariable Long companyId, @Valid @RequestBody AddAreaCompanyToCompanyRequest request) {

        var command = CompanyAssembler.toCommandFromRequest(companyId, request);
        this.companyCommandService.handle(command);

        var company = this.companyQueryService.handle(new GetCompanyByIdQuery(companyId));
        if (company.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(CompanyAssembler.toResponseFromEntity(company.get()));
    }

    /**
     *  Endpoint for retrieving Companies by their name. Accepts a name as a request parameter and returns a list of CompanyResponse objects representing the Companies that match the given name.
     * @param name  Name of the Companies to be retrieved.
     * @return  ResponseEntity containing a list of CompanyResponse objects representing the Companies that match the given name, or appropriate HTTP status code if no Companies are found with the given name.
     */
    @Operation(summary = "Get Companies by Name", description = "Retrieve a list of Companies that match the given name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Companies retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CompanyResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Companies found with the given name", content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<List<CompanyResponse>> getCompaniesByName(@RequestParam String name) {
        var query = new GetCompaniesByNameQuery(name);
        var companies = this.companyQueryService.handle(query);

        if (companies.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var companyResponses = companies.stream()
                .map(CompanyAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(companyResponses);
    }


}
