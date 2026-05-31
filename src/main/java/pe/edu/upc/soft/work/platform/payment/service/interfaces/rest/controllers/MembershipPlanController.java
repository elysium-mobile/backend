package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllMembershipPlanQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllMembershipQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetMembershipByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetMembershipPlanByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipCommandService;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipPlanCommandService;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipPlanQueryService;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipQueryService;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers.MembershipAssembler;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers.MembershipPlanAssembler;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/memberships-plans", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Membership Plans", description = "Endpoints for managing Memberships Plans")
public class MembershipPlanController {


    private final MembershipPlanCommandService membershipPlanCommandService;
    private final MembershipPlanQueryService membershipPlanQueryService;

    public MembershipPlanController(MembershipPlanCommandService membershipPlanCommandService, MembershipPlanQueryService membershipPlanQueryService){
        this.membershipPlanCommandService = membershipPlanCommandService;
        this.membershipPlanQueryService = membershipPlanQueryService;
    }

    @Operation(summary = "Create a new MembershipPlan", description = "Create a new MembershipPlan in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "MembershipPlan created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MembershipPlanResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Membership Plan not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<MembershipPlanResponse> createMembershipPlan(@RequestBody CreateMembershipPlanRequest request) {
        var createMembershipPlanCommand = MembershipPlanAssembler.toCommandFromRequest(request);
        var planId = this.membershipPlanCommandService.handle(createMembershipPlanCommand);

        if (Objects.isNull(planId) || planId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getMembershipPlanById = new GetMembershipPlanByIdQuery(planId);
        var plan = this.membershipPlanQueryService.handle(getMembershipPlanById);

        if (plan.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var membershipPlanResponse = MembershipPlanAssembler.toResponseFromEntity(plan.get());
        return new ResponseEntity<>(membershipPlanResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all Memberships Plan", description = "Retrieve a list of all Memberships Plan in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Memberships retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MembershipPlanResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Memberships found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<MembershipPlanResponse>> getAllMembershipsPlan() {
        var getAllMembershipPlanQuery = new GetAllMembershipPlanQuery();
        var memberships = this.membershipPlanQueryService.handle(getAllMembershipPlanQuery);

        var membershipResponses = memberships.stream()
                .map(MembershipPlanAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(membershipResponses);
    }

    @Operation(summary = "Get Membership Plan by ID", description = "Retrieve a Membership Plan by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membership Plan retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MembershipPlanResponse.class))),
            @ApiResponse(responseCode = "404", description = "Membership Plan not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MembershipPlanResponse> getMembershipPlanById(@PathVariable Long id) {
        var getMembershipPlanByIdQuery = new GetMembershipPlanByIdQuery(id);
        var membership = membershipPlanQueryService.handle(getMembershipPlanByIdQuery);

        if (membership.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var membershipResponse = MembershipPlanAssembler.toResponseFromEntity(membership.get());
        return ResponseEntity.ok(membershipResponse);
    }

    @Operation(summary = "Update Membership Plan information", description = "Update the information of an existing Membership Plan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membership Plan updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MembershipPlanResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Membership Plan not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<MembershipPlanResponse> updateMembership(@PathVariable Long id, @RequestBody UpdateMembershipPlanRequest request) {
        var updateMembershipPlanCommand = MembershipPlanAssembler.toCommandFromRequest(id, request);
        var updatedMembership = this.membershipPlanCommandService.handle(updateMembershipPlanCommand);
        if (updatedMembership.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var membershipResponse = MembershipPlanAssembler.toResponseFromEntity(updatedMembership.get());
        return ResponseEntity.ok(membershipResponse);
    }

    @Operation(summary = "Delete Membership Plan by ID", description = "Delete a Membership Plan by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membership Plan deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Membership Plan not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMembershipPlanById(@PathVariable Long id) {
        var deleteMembershipPlanCommand = new DeleteMembershipPlanCommand(id);
        this.membershipPlanCommandService.handle(deleteMembershipPlanCommand);
        return ResponseEntity.noContent().build();
    }

}
