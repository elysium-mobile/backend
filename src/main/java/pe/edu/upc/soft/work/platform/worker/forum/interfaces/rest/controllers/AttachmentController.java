package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.controllers;

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
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteAttachmentCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllAttachmentQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAttachmentByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.AttachmentCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.AttachmentQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers.AttachmentAssembler;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.AttachmentResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateAttachmentRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateAttachmentRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/attachments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Attachments", description = "Endpoints for managing Attachments")
public class AttachmentController {

    private final AttachmentCommandService attachmentCommandService;
    private final AttachmentQueryService attachmentQueryService;

    public AttachmentController(AttachmentCommandService attachmentCommandService, AttachmentQueryService attachmentQueryService) {
        this.attachmentCommandService = attachmentCommandService;
        this.attachmentQueryService = attachmentQueryService;
    }

    @Operation(summary = "Create a new Attachment", description = "Create a new Attachment in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Attachment created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AttachmentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Attachment not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AttachmentResponse> createAttachment(@RequestBody CreateAttachmentRequest request) {
        var createAttachmentCommand = AttachmentAssembler.toCommandFromRequest(request);
        var attachmentId = this.attachmentCommandService.handle(createAttachmentCommand);

        if (Objects.isNull(attachmentId) || attachmentId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getAttachmentById = new GetAttachmentByIdQuery(attachmentId);
        var attachment = this.attachmentQueryService.handle(getAttachmentById);

        if (attachment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var attachmentResponse = AttachmentAssembler.toResponseFromEntity(attachment.get());
        return new ResponseEntity<>(attachmentResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all Attachments", description = "Retrieve a list of all Attachments in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachments retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AttachmentResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Attachments found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<AttachmentResponse>> getAllAttachments() {
        var getAllAttachmentQuery = new GetAllAttachmentQuery();
        var attachments = this.attachmentQueryService.handle(getAllAttachmentQuery);

        var attachmentResponses = attachments.stream()
                .map(AttachmentAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(attachmentResponses);
    }

    @Operation(summary = "Get Attachment by ID", description = "Retrieve an Attachment by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AttachmentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Attachment not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AttachmentResponse> getAttachmentById(@PathVariable Long id) {
        var getAttachmentByIdQuery = new GetAttachmentByIdQuery(id);
        var attachment = attachmentQueryService.handle(getAttachmentByIdQuery);

        if (attachment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var attachmentResponse = AttachmentAssembler.toResponseFromEntity(attachment.get());
        return ResponseEntity.ok(attachmentResponse);
    }

    @Operation(summary = "Update Attachment information", description = "Update the information of an existing Attachment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AttachmentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Attachment not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AttachmentResponse> updateAttachment(@PathVariable Long id, @RequestBody UpdateAttachmentRequest request) {
        var updateAttachmentCommand = AttachmentAssembler.toCommandFromRequest(id, request);
        var updatedAttachment = this.attachmentCommandService.handle(updateAttachmentCommand);
        if (updatedAttachment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var attachmentResponse = AttachmentAssembler.toResponseFromEntity(updatedAttachment.get());
        return ResponseEntity.ok(attachmentResponse);
    }

    @Operation(summary = "Delete Attachment by ID", description = "Delete an Attachment by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Attachment deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Attachment not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttachmentById(@PathVariable Long id) {
        var deleteAttachmentCommand = new DeleteAttachmentCommand(id);
        this.attachmentCommandService.handle(deleteAttachmentCommand);
        return ResponseEntity.noContent().build();
    }
}
