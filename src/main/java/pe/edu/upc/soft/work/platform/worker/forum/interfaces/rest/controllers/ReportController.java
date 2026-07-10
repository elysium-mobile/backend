package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteReportCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllReportsQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetReportByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetReportsByUserAccountIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ReportCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ReportQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers.ReportAssembler;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateReportRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.ReportResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateReportRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for managing Reports in the system.
 * Provides endpoints for creating, retrieving, updating, and deleting Reports,
 * as well as filtering them by user account.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/reports", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reports", description = "Endpoints for managing Reports")
public class ReportController{

    private final ReportCommandService reportCommandService;
    private final ReportQueryService reportQueryService;

    /**
     * Constructor for ReportController.
     * Initializes the command and query services for handling Report operations.
     * @param reportCommandService Service for handling commands related to Reports
     * @param reportQueryService   Service for handling queries related to Reports
     */
    public ReportController(ReportCommandService reportCommandService,
                            ReportQueryService reportQueryService){
        this.reportCommandService = reportCommandService;
        this.reportQueryService= reportQueryService;
    }


    /**
     * Endpoint for creating a new Report.
     * @param request Request object containing the details of the Report to be created
     * @return ResponseEntity containing the created ReportResponse and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new Report", description = "Create a new Report in the system")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Report created successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ReportResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request", content = @io.swagger.v3.oas.annotations.media.Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Report not found", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PostMapping
    public ResponseEntity<ReportResponse> createReport(@Valid @RequestBody CreateReportRequest request){
        var createReportCommand = ReportAssembler.toCommandFromRequest(request);
        var reportId = this.reportCommandService.handle(createReportCommand);

        if (Objects.isNull(reportId) || reportId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getReportById = new GetReportByIdQuery(reportId);
        var report = this.reportQueryService.handle(getReportById);
        if (report.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var reportResponse = ReportAssembler.toResponseFromEntity(report.get());
        return new ResponseEntity<>(reportResponse, HttpStatus.CREATED);
    }


    /**
     * Endpoint for retrieving all Reports.
     * @return ResponseEntity containing a list of ReportResponse objects
     */
    @Operation(summary = "Get all Reports", description = "Retrieve a list of all Reports in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports retrieved successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ReportResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Reports found", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @GetMapping
    public ResponseEntity<List<ReportResponse>> getAllReports(){
        var getAllReportQuery = new GetAllReportsQuery();
        var reports = this.reportQueryService.handle(getAllReportQuery);

        var reportResponses = reports.stream()
                .map(ReportAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reportResponses);
    }

    /**
     * Endpoint for retrieving a specific Report by ID.
     * @param id ID of the Report to be retrieved
     * @return ResponseEntity containing the ReportResponse if found
     */
    @Operation(summary = "Get Report by ID", description = "Retrieve a Report by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report retrieved successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ReportResponse.class))),
            @ApiResponse(responseCode = "404", description = "Report not found", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable Long id){
        var getReportByIdQuery = new GetReportByIdQuery(id);
        var reports = reportQueryService.handle(getReportByIdQuery);

        if (reports.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var reportResponse = ReportAssembler.toResponseFromEntity(reports.get());
        return ResponseEntity.ok(reportResponse);
    }

    /**
     * Endpoint for updating an existing Report by ID.
     * @param id ID of the Report to be updated
     * @param request Request object containing the updated details
     * @return ResponseEntity containing the updated ReportResponse if successful
     */
    @Operation(summary = "Update Report by ID", description = "Update a Report by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report updated successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ReportResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "404", description = "Report not found", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReportResponse> updateReport(@PathVariable Long id, @Valid @RequestBody UpdateReportRequest request){
        var updateReportCommand = ReportAssembler.toCommandFromRequest(id, request);
        var updatedReport = this.reportCommandService.handle(updateReportCommand);
        if (updatedReport.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var reportResponse = ReportAssembler.toResponseFromEntity(updatedReport.get());
        return ResponseEntity.ok(reportResponse);
    }

    /**
     * Endpoint for deleting a Report by ID.
     * @param id ID of the Report to be deleted
     * @return ResponseEntity with no content if deleted successfully
     */
    @Operation(summary = "Delete Report by ID", description = "Delete a Report by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Report deleted successfully", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "404", description = "Report not found", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReportById(@PathVariable Long id){
        var deleteReportCommand= new DeleteReportCommand(id);
        this.reportCommandService.handle(deleteReportCommand);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for retrieving Reports by User Account ID.
     * @param userAccountId ID of the User Account
     * @return ResponseEntity containing a list of ReportResponse objects
     */
    @Operation(summary = "Get Reports by User Account ID", description = "Retrieve a list of Reports associated with a specific User Account ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports retrieved successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ReportResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Reports found for the specified User Account ID", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @GetMapping("/user-account/{userAccountId}")
    public ResponseEntity<List<ReportResponse>> getReportsByUserAccountId(@PathVariable Long userAccountId){
        var getReportsByUserAccount = new GetReportsByUserAccountIdQuery(userAccountId);
        var reports = this.reportQueryService.handle(getReportsByUserAccount);

        if (reports.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var reportResponses = reports.stream()
                .map(ReportAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reportResponses);
    }
}
