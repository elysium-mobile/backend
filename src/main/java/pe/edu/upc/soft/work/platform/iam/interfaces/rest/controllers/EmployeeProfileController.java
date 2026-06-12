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
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteEmployeeProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllEmployeeProfileQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetEmployeeProfileByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.services.EmployeeProfileCommandService;
import pe.edu.upc.soft.work.platform.iam.domain.services.EmployeeProfileQueryService;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers.EmployeeProfileAssembler;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.CreateEmployeeProfileRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.EmployeeProfileResponse;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UserResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(path = "/api/v1/employee-profile", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name="Employee Profiles", description = "Endpoints for managing employee profiles")
public class EmployeeProfileController {


    private final EmployeeProfileCommandService employeeProfileCommandService;
    private final EmployeeProfileQueryService employeeProfileQueryService;

    public EmployeeProfileController(EmployeeProfileCommandService employeeProfileCommandService, EmployeeProfileQueryService employeeProfileQueryService) {
        this.employeeProfileCommandService = employeeProfileCommandService;
        this.employeeProfileQueryService = employeeProfileQueryService;
    }



    @Operation(summary = "Create a new employee profile", description = "Create a new employee profile in the system",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Create employee profile request",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateEmployeeProfileRequest.class)
                    )
            ))

    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description = "Employee created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EmployeeProfileResponse> createEmployeeProfile(@RequestBody CreateEmployeeProfileRequest request) {
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

    @Operation(summary = "Get all users", description = "Retrieve a list of all users in the system")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
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

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their unique identifier",
            parameters = @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "The unique identifier of the user", required = true))
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
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
