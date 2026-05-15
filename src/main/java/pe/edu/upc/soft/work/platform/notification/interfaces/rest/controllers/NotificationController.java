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
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.DeleteNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetAllNotificationsQuery;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationByIdQuery;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationCommandService;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationQueryService;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.assemblers.NotificationAssembler;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.CreateNotificationRequest;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.NotificationResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.GET})
@RestController
@RequestMapping(value = "/api/v1/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name="Notifications", description = "Endpoints for managing notifications in the system")
public class NotificationController {

    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;

    public NotificationController(NotificationCommandService notificationCommandService, NotificationQueryService notificationQueryService){
        this.notificationCommandService = notificationCommandService;
        this.notificationQueryService = notificationQueryService;
    }

    @Operation(summary = "Create a new notification", description = "Create a new notification in the system",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Create notification request",
                required = true,
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CreateNotificationRequest.class))
        )
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Notification created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody CreateNotificationRequest request){
        var createNotificationCommand = NotificationAssembler.toCommandFromRequest(request);
        var notificationId = this.notificationCommandService.handle(createNotificationCommand);

        if (Objects.isNull(notificationId) || notificationId <= 0){
            return ResponseEntity.badRequest().build();
        }

        var getNotificationById = new GetNotificationByIdQuery(notificationId);
        var notification = this.notificationQueryService.handle(getNotificationById);

        if (notification.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var notificationResponse = NotificationAssembler.toResponseFromEntity(notification.get());
        return new ResponseEntity<>(notificationResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all notifications", description = "Retrieve a list of all notifications in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No notifications found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications(){
        var getAllNotificationQuery = new GetAllNotificationsQuery();
        var notifications = this.notificationQueryService.handle(getAllNotificationQuery);

        var notificationResponse= notifications.stream()
                .map(NotificationAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(notificationResponse);
    }


    @Operation(summary = "Get notification by ID", description = "Retrieve a notification by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification retrieved successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content)
    })
    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long notificationId) {
        var getNotificationById = new GetNotificationByIdQuery(notificationId);
        var notification = this.notificationQueryService.handle(getNotificationById);

        if (notification.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var notificationResponse = NotificationAssembler.toResponseFromEntity(notification.get());
        return ResponseEntity.ok(notificationResponse);
    }

    @Operation(summary = "Update a notification", description = "Update an existing notification in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification updated successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content)

    })
    @PutMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> updateNotification(@PathVariable Long notificationId, @RequestBody CreateNotificationRequest request){
        var updateNotificationCommand = NotificationAssembler.toCommandFromRequest(notificationId, request);
        var updatedNotification = this.notificationCommandService.handle(updateNotificationCommand);

        if (updatedNotification.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        var notificationResponse = NotificationAssembler.toResponseFromEntity(updatedNotification.get());
        return ResponseEntity.ok(notificationResponse);
    }

    @Operation(summary = "Delete a notification", description = "Delete an existing notification from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content)
    })
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<?> deleteNotificationById(@PathVariable Long notificationId){
        var deleteNotificationCommand = new DeleteNotificationCommand(notificationId);
        this.notificationCommandService.handle(deleteNotificationCommand);
        return ResponseEntity.noContent().build();
    }


}
