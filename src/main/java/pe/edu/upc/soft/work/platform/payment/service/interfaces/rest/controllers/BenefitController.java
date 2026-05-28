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
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllBenefitQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetBenefitByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.BenefitCommandService;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.BenefitQueryService;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers.BenefitAssembler;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.BenefitResponse;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreateBenefitRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.UpdateBenefitRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/benefits", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Benefits", description = "Endpoints for managing Benefits")
public class BenefitController {

    private final BenefitCommandService benefitCommandService;
    private final BenefitQueryService benefitQueryService;

    public BenefitController(BenefitCommandService benefitCommandService, BenefitQueryService benefitQueryService){
        this.benefitCommandService = benefitCommandService;
        this.benefitQueryService = benefitQueryService;
    }

    @Operation(summary = "Create a new Benefit", description = "Create a new Benefit in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Benefit created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BenefitResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Benefit not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<BenefitResponse> createBenefit(@RequestBody CreateBenefitRequest request){
        var createBenefitCommand = BenefitAssembler.toCommandFromRequest(request);
        var benefitId = this.benefitCommandService.handle(createBenefitCommand);

        if (Objects.isNull(benefitId)){
            return ResponseEntity.badRequest().build();
        }

        var getBenefitById = new GetBenefitByIdQuery(benefitId);
        var benefit = this.benefitQueryService.handle(getBenefitById);

        if (benefit.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var benefitResponse = BenefitAssembler.toResponseFromEntity(benefit.get());
        return new ResponseEntity<>(benefitResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all Benefits", description = "Retrieve a list of all Benefits in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Benefits retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BenefitResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Benefits found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<BenefitResponse>> getAllBenefits(){
        var getAllBenefitQuery = new GetAllBenefitQuery();
        var benefit = this.benefitQueryService.handle(getAllBenefitQuery);

        var benefitResponses= benefit.stream()
                .map(BenefitAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(benefitResponses);
    }

    @Operation(summary = "Get Benefits by ID", description = "Retrieve a Benefits by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Benefits retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BenefitResponse.class))),
            @ApiResponse(responseCode = "404", description = "Benefits not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BenefitResponse> getBenefitById(@PathVariable Long id){
        var getBenefitByIdQuery= new GetBenefitByIdQuery(id);
        var benefit = benefitQueryService.handle(getBenefitByIdQuery);

        if (benefit.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var benefitResponse = BenefitAssembler.toResponseFromEntity(benefit.get());
        return ResponseEntity.ok(benefitResponse);
    }

    @Operation(summary = "Update Benefit information", description = "Update the information of an existing Benefit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Benefit updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BenefitResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Benefit not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<BenefitResponse> updateBenefit(@PathVariable Long id, @RequestBody UpdateBenefitRequest request){
        var updateBenefitCommand = BenefitAssembler.toCommandFromRequest(id,request);
        var updatedBenefit = this.benefitCommandService.handle(updateBenefitCommand);
        if (updatedBenefit.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var benefitResponse = BenefitAssembler.toResponseFromEntity(updatedBenefit.get());
        return ResponseEntity.ok(benefitResponse);
    }

    @Operation(summary = "Delete Benefit by ID", description = "Delete a Benefit by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Benefit deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Benefit not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBenefitById(@PathVariable Long id){
        var deleteBenefitCommand = new DeleteBenefitCommand(id);
        this.benefitCommandService.handle(deleteBenefitCommand);
        return ResponseEntity.noContent().build();
    }
}
