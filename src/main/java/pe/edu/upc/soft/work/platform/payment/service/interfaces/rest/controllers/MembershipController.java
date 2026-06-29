package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.controllers;

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
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetMembershipByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllMembershipQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipCommandService;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipQueryService;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers.MembershipAssembler;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreateMembershipRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.UpdateMembershipRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.MembershipResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for managing Memberships in the system.
 * Provides endpoints for creating, retrieving, updating, and deleting Memberships.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/memberships", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Memberships", description = "Endpoints for managing Memberships")
public class MembershipController {

    private final MembershipCommandService membershipCommandService;
    private final MembershipQueryService membershipQueryService;

    /**
     * Constructor for MembershipController.
     * Initializes the command and query services for handling Membership operations.
     * @param membershipCommandService Service for handling commands related to Memberships
     * @param membershipQueryService   Service for handling queries related to Memberships
     */
    public MembershipController(MembershipCommandService membershipCommandService, MembershipQueryService membershipQueryService) {
        this.membershipCommandService = membershipCommandService;
        this.membershipQueryService = membershipQueryService;
    }

    /**
     * Endpoint for creating a new Membership.
     * @param request Request object containing the details of the Membership to be created
     * @return ResponseEntity containing the created MembershipResponse and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new Membership", description = "Create a new Membership in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Membership created successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                            schema = @Schema(implementation = MembershipResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Membership not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<MembershipResponse> createMembership(@RequestBody CreateMembershipRequest request) {
        var createMembershipCommand = MembershipAssembler.toCommandFromRequest(request);
        var membershipId = this.membershipCommandService.handle(createMembershipCommand);

        if (Objects.isNull(membershipId) || membershipId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getMembershipById = new GetMembershipByIdQuery(membershipId);
        var membership = this.membershipQueryService.handle(getMembershipById);

        if (membership.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var membershipResponse = MembershipAssembler.toResponseFromEntity(membership.get());
        return new ResponseEntity<>(membershipResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving all Memberships.
     * @return ResponseEntity containing a list of MembershipResponse objects
     */
    @Operation(summary = "Get all Memberships", description = "Retrieve a list of all Memberships in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Memberships retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MembershipResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Memberships found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<MembershipResponse>> getAllMemberships() {
        var getAllMembershipQuery = new GetAllMembershipQuery();
        var memberships = this.membershipQueryService.handle(getAllMembershipQuery);

        var membershipResponses = memberships.stream()
                .map(MembershipAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(membershipResponses);
    }

    /**
     * Endpoint for retrieving a specific Membership by ID.
     * @param id ID of the Membership to be retrieved
     * @return ResponseEntity containing the MembershipResponse if found
     */
    @Operation(summary = "Get Membership by ID", description = "Retrieve a Membership by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membership retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MembershipResponse.class))),
            @ApiResponse(responseCode = "404", description = "Membership not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MembershipResponse> getMembershipById(@PathVariable Long id) {
        var getMembershipByIdQuery = new GetMembershipByIdQuery(id);
        var membership = membershipQueryService.handle(getMembershipByIdQuery);

        if (membership.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var membershipResponse = MembershipAssembler.toResponseFromEntity(membership.get());
        return ResponseEntity.ok(membershipResponse);
    }

    /**
     * Endpoint for updating an existing Membership by ID.
     * @param id ID of the Membership to be updated
     * @param request Request object containing the updated details
     * @return ResponseEntity containing the updated MembershipResponse if successful
     */
    @Operation(summary = "Update Membership information", description = "Update the information of an existing Membership")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membership updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MembershipResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Membership not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<MembershipResponse> updateMembership(@PathVariable Long id, @RequestBody UpdateMembershipRequest request) {
        var updateMembershipCommand = MembershipAssembler.toCommandFromRequest(id, request);
        var updatedMembership = this.membershipCommandService.handle(updateMembershipCommand);
        if (updatedMembership.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var membershipResponse = MembershipAssembler.toResponseFromEntity(updatedMembership.get());
        return ResponseEntity.ok(membershipResponse);
    }

    /**
     * Endpoint for deleting a Membership by ID.
     * @param id ID of the Membership to be deleted
     * @return ResponseEntity with no content if deleted successfully
     */
    @Operation(summary = "Delete Membership by ID", description = "Delete a Membership by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membership deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Membership not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMembershipById(@PathVariable Long id) {
        var deleteMembershipCommand = new DeleteMembershipCommand(id);
        this.membershipCommandService.handle(deleteMembershipCommand);
        return ResponseEntity.noContent().build();
    }
}
