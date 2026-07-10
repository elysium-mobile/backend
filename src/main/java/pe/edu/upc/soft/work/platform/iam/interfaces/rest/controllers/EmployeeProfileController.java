package pe.edu.upc.soft.work.platform.iam.interfaces.rest.controllers;


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
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteEmployeeProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllEmployeeProfileQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetEmployeeProfileByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.services.EmployeeProfileCommandService;
import pe.edu.upc.soft.work.platform.iam.domain.services.EmployeeProfileQueryService;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers.EmployeeProfileAssembler;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.CreateEmployeeProfileRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.EmployeeProfileResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for managing employee profiles in the system.
 * Provides endpoints for creating, retrieving, and deleting employee profiles.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(path = "/api/v1/employee-profile", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name="Employee Profiles", description = "Endpoints for managing employee profiles")
public class EmployeeProfileController {


    private final EmployeeProfileCommandService employeeProfileCommandService;
    private final EmployeeProfileQueryService employeeProfileQueryService;

    /**
     * Constructor for EmployeeProfileController.
     * Initializes the command and query services for handling employee profile operations.
     * @param employeeProfileCommandService Service for handling commands related to employee profiles
     * @param employeeProfileQueryService   Service for handling queries related to employee profiles
     */
    public EmployeeProfileController(EmployeeProfileCommandService employeeProfileCommandService, EmployeeProfileQueryService employeeProfileQueryService) {
        this.employeeProfileCommandService = employeeProfileCommandService;
        this.employeeProfileQueryService = employeeProfileQueryService;
    }

    /**
     * Endpoint for creating a new employee profile.
     * @param request Request object containing the details of the employee profile to be created
     * @return ResponseEntity containing the created employee profile and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new employee profile", description = "Create a new employee profile in the system",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Create employee profile request",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateEmployeeProfileRequest.class)
                    )
            ))

    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description = "Employee created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EmployeeProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EmployeeProfileResponse> createEmployeeProfile(@Valid @RequestBody CreateEmployeeProfileRequest request) {
        var createEmployeeProfileCommand = EmployeeProfileAssembler.toCommandFromRequest(request);
        var profileId = this.employeeProfileCommandService.handle(createEmployeeProfileCommand);

        if (Objects.isNull(profileId) || profileId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getEmployeeById = new GetEmployeeProfileByIdQuery(profileId);
        var employeeProfile = this.employeeProfileQueryService.handle(getEmployeeById);

        if(employeeProfile.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        var response = EmployeeProfileAssembler.toResponseFromEntity(employeeProfile.get());
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    /**
     * Endpoint for retrieving all employee profiles.
     * @return ResponseEntity containing a list of EmployeeProfileResponse objects
     */
    @Operation(summary = "Get all users", description = "Retrieve a list of all users in the system")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmployeeProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "No users found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<EmployeeProfileResponse>> getAllEmployeeProfiles() {
        var getAllEmployeeProfiles = new GetAllEmployeeProfileQuery();
        var employeeProfiles = this.employeeProfileQueryService.handle(getAllEmployeeProfiles);
        var response = employeeProfiles.stream()
                .map(EmployeeProfileAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for retrieving a specific employee profile by ID.
     * @param id The unique identifier of the employee profile
     * @return ResponseEntity containing the EmployeeProfileResponse if found
     */
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their unique identifier",
            parameters = @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "The unique identifier of the user", required = true))
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmployeeProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeProfileResponse> getEmployeeProfile(@PathVariable Long id){
        var getEmployeeProfileByIdQuery = new GetEmployeeProfileByIdQuery(id);
        var employees = employeeProfileQueryService.handle(getEmployeeProfileByIdQuery);

        if(employees.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        var employeeResponse = EmployeeProfileAssembler.toResponseFromEntity(employees.get());
        return ResponseEntity.ok(employeeResponse);
    }


    /**
     * Endpoint for deleting an employee profile by ID.
     * @param employeeProfileId The unique identifier of the employee profile to be deleted
     * @return ResponseEntity with no content if deleted successfully
     */
    @Operation(summary = "Delete user by ID", description = "Delete a user by their unique identifier",
            parameters = @io.swagger.v3.oas.annotations.Parameter(name = "userId", description =
                    "The unique identifier of the user to be deleted", required = true))
    @ApiResponses(value ={
            @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/{employeeProfileId}")
    public ResponseEntity<?> deleteEmployeeProfileById(@PathVariable Long employeeProfileId){
        var deleteEmployeeCommand = new DeleteEmployeeProfileCommand(employeeProfileId);
        this.employeeProfileCommandService.handle(deleteEmployeeCommand);
        return ResponseEntity.noContent().build();
    }
}
