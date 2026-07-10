package pe.edu.upc.soft.work.platform.notification.interfaces.rest.controllers;


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
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.DeleteNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetAllNotificationDetailQuery;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationByIdQuery;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationDetailByIdQuery;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationDetailCommandService;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationDetailQueryService;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.assemblers.NotificationDetailAssembler;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.CreateNotificationDetailRequest;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.NotificationDetailResponse;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.UpdateNotificationDetailRequest;

import java.util.List;
import java.util.Objects;

/**
 * Controller for managing notification details in the system.
 * Provides endpoints for creating, retrieving, updating, and deleting notification details.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/notification-details", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Notification Details", description = "Endpoints for managing notification details in the system")
public class NotificationDetailController {

    private final NotificationDetailCommandService notificationDetailCommandService;
    private final NotificationDetailQueryService notificationDetailQueryService;

    /**
     * Constructor for NotificationDetailController.
     * Initializes the command and query services for handling notification detail operations.
     * @param notificationDetailCommandService Service for handling commands related to notification details
     * @param notificationDetailQueryService   Service for handling queries related to notification details
     */
    public NotificationDetailController(NotificationDetailCommandService notificationDetailCommandService, NotificationDetailQueryService notificationDetailQueryService) {
        this.notificationDetailCommandService = notificationDetailCommandService;
        this.notificationDetailQueryService = notificationDetailQueryService;
    }

    /**
     * Endpoint for creating a new notification detail.
     * @param request Request object containing the details of the notification detail to be created
     * @return ResponseEntity containing the created NotificationDetailResponse and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new notification detail", description = "Create a new notification detail in the system",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Create notification detail request",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CreateNotificationDetailRequest.class))
            )
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Notification detail created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationDetailResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification detail not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<NotificationDetailResponse> createNotificationDetail(@Valid @RequestBody CreateNotificationDetailRequest request) {
        var createNotificationDetailCommand = NotificationDetailAssembler.toCommandFromRequest(request);
        var notificationDetailId = this.notificationDetailCommandService.handle(createNotificationDetailCommand);

        if (Objects.isNull(notificationDetailId) || notificationDetailId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var getNotificationDetailById = new GetNotificationDetailByIdQuery(notificationDetailId);
        var notificationDetail = this.notificationDetailQueryService.handle(getNotificationDetailById);

        if (notificationDetail.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var notificationDetailResponse = NotificationDetailAssembler.toResponseFromEntity(notificationDetail.get());
        return new ResponseEntity<>(notificationDetailResponse, HttpStatus.CREATED);
    }


    /**
     * Endpoint for retrieving all notification details.
     * @return ResponseEntity containing a list of NotificationDetailResponse objects
     */
    @Operation(summary = "Get all notifications details", description = "Retrieve a list of all notification details in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification details retrieved successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Notification details not found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<NotificationDetailResponse>> getAllNotificationDetails() {
        var getAllNotificationDetailQuery = new GetAllNotificationDetailQuery();
        var notificationDetails = this.notificationDetailQueryService.handle(getAllNotificationDetailQuery);

        var notificationsDetails = notificationDetails.stream().map(NotificationDetailAssembler::toResponseFromEntity).toList();
        return ResponseEntity.ok(notificationsDetails);
    }

    /**
     * Endpoint for retrieving a specific notification detail by ID.
     * @param notificationDetailId ID of the notification detail to be retrieved
     * @return ResponseEntity containing the NotificationDetailResponse if found
     */
    @Operation(summary = "Get notification detail by id", description = "Retrieve a notification detail by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification detail retrieved successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Notification detail not found", content = @Content)
    })
    @GetMapping("/{notificationDetailId}")
    public ResponseEntity<NotificationDetailResponse> getNotificationDetailById(@PathVariable Long notificationDetailId) {
        var getNotificationDetailById = new GetNotificationDetailByIdQuery(notificationDetailId);
        var notificationDetail = this.notificationDetailQueryService.handle(getNotificationDetailById);

        if (notificationDetail.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var notificationDetailResponse = NotificationDetailAssembler.toResponseFromEntity(notificationDetail.get());
        return ResponseEntity.ok(notificationDetailResponse);
    }


    /**
     * Endpoint for updating an existing notification detail by ID.
     * @param notificationDetailId ID of the notification detail to be updated
     * @param request Request object containing the updated details
     * @return ResponseEntity containing the updated NotificationDetailResponse if successful
     */
    @Operation(summary = "Update a notification detail", description = "Update an existing notification detail in the system",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Update notification detail request",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CreateNotificationDetailRequest.class))
            )
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification detail updated successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UpdateNotificationDetailRequest.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification detail not found", content = @Content)
    })
    @PutMapping("/{notificationDetailId}")
    public ResponseEntity<NotificationDetailResponse> updateNotificationDetail(@PathVariable Long notificationDetailId, @Valid @RequestBody UpdateNotificationDetailRequest request){
        var updateNotificationDetailCommand = NotificationDetailAssembler.toCommandFromRequest(notificationDetailId, request);
        var updatedNotificationDetailId = this.notificationDetailCommandService.handle(updateNotificationDetailCommand);
        if (updatedNotificationDetailId.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var notificationDetailResponse = NotificationDetailAssembler.toResponseFromEntity(updatedNotificationDetailId.get());
        return ResponseEntity.ok(notificationDetailResponse);
    }

    /**
     * Endpoint for deleting a notification detail by ID.
     * @param detailId ID of the notification detail to be deleted
     * @return ResponseEntity with no content if deleted successfully
     */
    @Operation(summary = "Delete a notification detail", description = "Delete an existing notification detail in the system",
            parameters = @io.swagger.v3.oas.annotations.Parameter(name = "detailId", description = "The unique identifier of the notification detail to be deleted", required = true)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification detail deleted successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Notification detail not found", content = @Content)
    })
    @DeleteMapping("/{detailId}")
    public ResponseEntity<?> deleteNotificationDetailById(@PathVariable Long detailId){
        var deleteNotificationDetailCommand = new DeleteNotificationDetailCommand(detailId);
        this.notificationDetailCommandService.handle(deleteNotificationDetailCommand);
        return ResponseEntity.noContent().build();
    }

}
