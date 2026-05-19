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
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.DeleteNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetAllNotificationDetailQuery;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationByIdQuery;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationDetailByIdQuery;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationDetailCommandService;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationDetailQueryService;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.assemblers.NotificationDetailAssembler;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.CreateNotificationDetailRequest;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.NotificationDetailResponse;

import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/notification-details", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Notification Details", description = "Endpoints for managing notification details in the system")
public class NotificationDetailController {

    private final NotificationDetailCommandService notificationDetailCommandService;
    private final NotificationDetailQueryService notificationDetailQueryService;

    public NotificationDetailController(NotificationDetailCommandService notificationDetailCommandService, NotificationDetailQueryService notificationDetailQueryService) {
        this.notificationDetailCommandService = notificationDetailCommandService;
        this.notificationDetailQueryService = notificationDetailQueryService;
    }

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
    public ResponseEntity<NotificationDetailResponse> createNotificationDetail(@RequestBody CreateNotificationDetailRequest request) {
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


    @Operation(summary = "Get all notifications details", description = "Retrieve a list of all notification details in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification details retrieved successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Notification details not found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<NotificationDetailResponse>> getAllNotifications() {
        var getAllNotificationDetailQuery = new GetAllNotificationDetailQuery();
        var notificationDetails = this.notificationDetailQueryService.handle(getAllNotificationDetailQuery);

        var notificationsDetails = notificationDetails.stream().map(NotificationDetailAssembler::toResponseFromEntity).toList();
        return ResponseEntity.ok(notificationsDetails);
    }

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


    @Operation(summary = "Update a notification detail", description = "Update an existing notification detail in the system",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Update notification detail request",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CreateNotificationDetailRequest.class))
            )
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification detail updated successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationDetailResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification detail not found", content = @Content)
    })
    @PutMapping("/{notificationDetailId}")
    public ResponseEntity<NotificationDetailResponse> updateNotificationDetail(@PathVariable Long notificationDetailId, @RequestBody CreateNotificationDetailRequest request){
        var updateNotificationDetailCommand = NotificationDetailAssembler.toCommandFromRequest(notificationDetailId, request);
        var updatedNotificationDetailId = this.notificationDetailCommandService.handle(updateNotificationDetailCommand);
        if (updatedNotificationDetailId.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var notificationDetailResponse = NotificationDetailAssembler.toResponseFromEntity(updatedNotificationDetailId.get());
        return ResponseEntity.ok(notificationDetailResponse);
    }

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
