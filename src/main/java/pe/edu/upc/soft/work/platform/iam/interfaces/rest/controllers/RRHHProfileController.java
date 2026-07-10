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
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllRRHHProfileQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetRRHHProfileByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.services.RRHHProfileCommandService;
import pe.edu.upc.soft.work.platform.iam.domain.services.RRHHProfileQueryService;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers.RRHHProfileAssembler;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.CreateRRHHProfileRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.RRHHProfileResponse;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UpdateRRHHProfileRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for managing RRHH profiles in the system.
 * Provides endpoints for creating, retrieving, updating, and deleting RRHH profiles.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/rrhh-profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "RRHH Profiles", description = "Endpoints for managing rrhh profiles in the IAM system")
public class RRHHProfileController {

    private final RRHHProfileCommandService rrhhProfileCommandService;
    private final RRHHProfileQueryService rrhhProfileQueryService;

    /**
     * Constructor for RRHHProfileController.
     * Initializes the command and query services for handling RRHH profile operations.
     * @param rrhhProfileCommandService Service for handling commands related to RRHH profiles
     * @param rrhhProfileQueryService   Service for handling queries related to RRHH profiles
     */
    public RRHHProfileController(RRHHProfileCommandService rrhhProfileCommandService, RRHHProfileQueryService rrhhProfileQueryService){
        this.rrhhProfileCommandService = rrhhProfileCommandService;
        this.rrhhProfileQueryService = rrhhProfileQueryService;
    }

    /**
     * Endpoint for creating a new RRHH profile.
     * @param request Request object containing the details of the RRHH profile to be created
     * @return ResponseEntity containing the created RRHH profile and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new rrhh profile", description = "Create a new rrhh profile in the system",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Create rrhhProfile request",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateRRHHProfileRequest.class)
                    )
            ))
    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description = "RRHHProfile created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RRHHProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "RRHHProfile not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<RRHHProfileResponse> createRRHHProfile(@Valid @RequestBody CreateRRHHProfileRequest request){
        var createRRHHCommand = RRHHProfileAssembler.toCommandFromRequest(request);
        var rrhhId = this.rrhhProfileCommandService.handle(createRRHHCommand);

        if (Objects.isNull(rrhhId)){
            return ResponseEntity.badRequest().build();
        }

        var getRRHHprofileById = new GetRRHHProfileByIdQuery(rrhhId);
        var profile = this.rrhhProfileQueryService.handle(getRRHHprofileById);

        if (profile.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var rrhhResponse = RRHHProfileAssembler.toResponseFromEntity(profile.get());
        return new ResponseEntity<>(rrhhResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving all RRHH profiles.
     * @return ResponseEntity containing a list of RRHHProfileResponse objects
     */
    @Operation(summary = "Get all RRHHProfiles", description = "Retrieve a list of all RRHHProfile in the system")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "RRHHProfiles retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RRHHProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "No RRHHProfiles found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<RRHHProfileResponse>> getAllRRHHProfiles(){
        var getAllRRHHProfilesQuery = new GetAllRRHHProfileQuery();
        var profiles = this.rrhhProfileQueryService.handle(getAllRRHHProfilesQuery);

        var rrhhProfileResponse = profiles.stream()
                .map(RRHHProfileAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rrhhProfileResponse);
    }

    /**
     * Endpoint for retrieving a specific RRHH profile by ID.
     * @param id The unique identifier of the RRHH profile
     * @return ResponseEntity containing the RRHHProfileResponse if found
     */
    @Operation(summary = "Get RRHHProfile by ID", description = "Retrieve a RRHHProfile by their unique identifier",
            parameters = @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "The unique identifier of the RRHHProfile", required = true))
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "RRHHProfile retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RRHHProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "RRHHProfile not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RRHHProfileResponse> getRRHHProfilesById(@PathVariable Long id){
        var getRRHHProfileByIdQuery =new GetRRHHProfileByIdQuery(id);
        var profiles = rrhhProfileQueryService.handle(getRRHHProfileByIdQuery);

        if (profiles.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var profilesResponse = RRHHProfileAssembler.toResponseFromEntity(profiles.get());
        return ResponseEntity.ok(profilesResponse);
    }

    /**
     * Endpoint for updating an existing RRHH profile by ID.
     * @param id The unique identifier of the RRHH profile
     * @param request Request object containing the updated details
     * @return ResponseEntity containing the updated RRHHProfileResponse if successful
     */
    @Operation(summary = "Update RRHHProfile information", description = "Update the information of an existing RRHHProfile")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "RRHHProfile updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RRHHProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "RRHHProfile not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<RRHHProfileResponse> updateRRHHProfile(@PathVariable Long id, @Valid @RequestBody UpdateRRHHProfileRequest request)
    {
        var updateRRHHCommand = RRHHProfileAssembler.toCommandFromRequest(id,request);
        var updateRRHH = this.rrhhProfileCommandService.handle(updateRRHHCommand);
        if (updateRRHH.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var RRHHResponse= RRHHProfileAssembler.toResponseFromEntity(updateRRHH.get());
        return ResponseEntity.ok(RRHHResponse);
    }

    /**
     * Endpoint for deleting an RRHH profile by ID.
     * @param id The unique identifier of the RRHH profile to be deleted
     * @return ResponseEntity with no content if deleted successfully
     */
    @Operation(summary = "Delete RRHHProfile by ID", description = "Delete a RRHHProfile by their unique identifier",
            parameters = @io.swagger.v3.oas.annotations.Parameter(name = "id", description =
                    "The unique identifier of the RRHHProfile to be deleted", required = true))
    @ApiResponses(value ={
            @ApiResponse(responseCode = "204", description = "RRHHProfile deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "RRHHProfile not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRRHHProfileById(@PathVariable Long id){
        var deleteRRHHCommand = new DeleteRRHHProfileCommand(id);
        this.rrhhProfileCommandService.handle(deleteRRHHCommand);
        return ResponseEntity.noContent().build();
    }
}
