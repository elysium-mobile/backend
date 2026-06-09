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
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllAssetsQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAssetByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.AssetCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.AssetQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers.AssetAssembler;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.AssetResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateAssetRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateAssetRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/attachments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Attachments", description = "Endpoints for managing Attachments")
public class AssetController {

    private final AssetCommandService assetCommandService;
    private final AssetQueryService assetQueryService;

    public AssetController(AssetCommandService assetCommandService, AssetQueryService assetQueryService) {
        this.assetCommandService = assetCommandService;
        this.assetQueryService = assetQueryService;
    }

    @Operation(summary = "Create a new Attachment", description = "Create a new Attachment in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Attachment created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Attachment not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AssetResponse> createAttachment(@RequestBody CreateAssetRequest request) {
        var createAttachmentCommand = AssetAssembler.toCommandFromRequest(request);
        var attachmentId = this.assetCommandService.handle(createAttachmentCommand);

        if (Objects.isNull(attachmentId) || attachmentId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getAttachmentById = new GetAssetByIdQuery(attachmentId);
        var attachment = this.assetQueryService.handle(getAttachmentById);

        if (attachment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var attachmentResponse = AssetAssembler.toResponseFromEntity(attachment.get());
        return new ResponseEntity<>(attachmentResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all Attachments", description = "Retrieve a list of all Attachments in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachments retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Attachments found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<AssetResponse>> getAllAttachments() {
        var getAllAttachmentQuery = new GetAllAssetsQuery();
        var attachments = this.assetQueryService.handle(getAllAttachmentQuery);

        var attachmentResponses = attachments.stream()
                .map(AssetAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(attachmentResponses);
    }

    @Operation(summary = "Get Attachment by ID", description = "Retrieve an Attachment by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetResponse.class))),
            @ApiResponse(responseCode = "404", description = "Attachment not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AssetResponse> getAttachmentById(@PathVariable Long id) {
        var getAttachmentByIdQuery = new GetAssetByIdQuery(id);
        var attachment = assetQueryService.handle(getAttachmentByIdQuery);

        if (attachment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var attachmentResponse = AssetAssembler.toResponseFromEntity(attachment.get());
        return ResponseEntity.ok(attachmentResponse);
    }

    @Operation(summary = "Update Attachment information", description = "Update the information of an existing Attachment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Attachment not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AssetResponse> updateAttachment(@PathVariable Long id, @RequestBody UpdateAssetRequest request) {
        var updateAttachmentCommand = AssetAssembler.toCommandFromRequest(id, request);
        var updatedAttachment = this.assetCommandService.handle(updateAttachmentCommand);
        if (updatedAttachment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var attachmentResponse = AssetAssembler.toResponseFromEntity(updatedAttachment.get());
        return ResponseEntity.ok(attachmentResponse);
    }

    @Operation(summary = "Delete Attachment by ID", description = "Delete an Attachment by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Attachment deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Attachment not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttachmentById(@PathVariable Long id) {
        var deleteAttachmentCommand = new DeleteAssetCommand(id);
        this.assetCommandService.handle(deleteAttachmentCommand);
        return ResponseEntity.noContent().build();
    }
}
