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
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllAssetsQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAssetByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.AssetCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.AssetQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers.AssetAssembler;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.AssetResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateAssetRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateAssetRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Controller for managing Assets in the system.
 * Provides endpoints for uploading files (assets), retrieving, updating, and deleting asset records.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/assets", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Assets", description = "Endpoints for managing Assets")
public class AssetController {

    private final AssetCommandService assetCommandService;
    private final AssetQueryService assetQueryService;

    /**
     * Constructor for AssetController.
     * Initializes the command and query services for handling Asset operations.
     * @param assetCommandService Service for handling commands related to Assets
     * @param assetQueryService   Service for handling queries related to Assets
     */
    public AssetController(AssetCommandService assetCommandService, AssetQueryService assetQueryService) {
        this.assetCommandService = assetCommandService;
        this.assetQueryService = assetQueryService;
    }

    /**
     * Endpoint for creating a new Asset by uploading a file.
     * @param messageId ID of the message associated with the asset
     * @param name      Name of the asset
     * @param fileType  Type of the file (e.g., IMAGE, DOCUMENT)
     * @param file      The file content to upload
     * @return ResponseEntity containing the created AssetResponse and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new Asset", description = "Upload a file to Cloudinary and register the Asset")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Asset created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AssetResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Asset not found",  content = @Content)
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AssetResponse> createAsset(
        @RequestParam("messageId") Long          messageId,
        @RequestParam("name")      String         name,
        @RequestParam("fileType")  FileType       fileType,
        @RequestParam("file")      MultipartFile  file) {

        var createAssetCommand = new CreateAssetCommand(messageId, name, fileType);
        var assetId = this.assetCommandService.handle(createAssetCommand, file);

        if (Objects.isNull(assetId) || assetId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        var asset = this.assetQueryService.handle(new GetAssetByIdQuery(assetId));
        if (asset.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(AssetAssembler.toResponseFromEntity(asset.get()), HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving all Assets.
     * @return ResponseEntity containing a list of AssetResponse objects
     */
    @Operation(summary = "Get all Asset", description = "Retrieve a list of all Asset in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Asset found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<AssetResponse>> getAllAssets() {
        var getAllAssetsQuery = new GetAllAssetsQuery();
        var assets = this.assetQueryService.handle(getAllAssetsQuery);

        var assetResponses = assets.stream()
                .map(AssetAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(assetResponses);
    }

    /**
     * Endpoint for retrieving a specific Asset by ID.
     * @param id ID of the Asset to be retrieved
     * @return ResponseEntity containing the AssetResponse if found
     */
    @Operation(summary = "Get Asset by ID", description = "Retrieve an Asset by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetResponse.class))),
            @ApiResponse(responseCode = "404", description = "Asset not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AssetResponse> getAssetById(@PathVariable Long id) {
        var getAssetByIdQuery = new GetAssetByIdQuery(id);
        var asset = assetQueryService.handle(getAssetByIdQuery);

        if (asset.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var assetResponse = AssetAssembler.toResponseFromEntity(asset.get());
        return ResponseEntity.ok(assetResponse);
    }

    /**
     * Endpoint for updating an existing Asset by ID.
     * @param id ID of the Asset to be updated
     * @param request Request object containing the updated details
     * @return ResponseEntity containing the updated AssetResponse if successful
     */
    @Operation(summary = "Update Asset information", description = "Update the information of an existing Asset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Asset not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AssetResponse> updateAsset(@PathVariable Long id, @Valid @RequestBody UpdateAssetRequest request) {
        var updateAssetCommand = AssetAssembler.toCommandFromRequest(id, request);
        var updatedAsset = this.assetCommandService.handle(updateAssetCommand);
        if (updatedAsset.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var assetResponse = AssetAssembler.toResponseFromEntity(updatedAsset.get());
        return ResponseEntity.ok(assetResponse);
    }

    /**
     * Endpoint for deleting an Asset by ID.
     * @param id ID of the Asset to be deleted
     * @return ResponseEntity with no content if deleted successfully
     */
    @Operation(summary = "Delete Asset by ID", description = "Delete an Asset by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Asset deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Asset not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssetById(@PathVariable Long id) {
        var deleteAssetCommand = new DeleteAssetCommand(id);
        this.assetCommandService.handle(deleteAssetCommand);
        return ResponseEntity.noContent().build();
    }


}
