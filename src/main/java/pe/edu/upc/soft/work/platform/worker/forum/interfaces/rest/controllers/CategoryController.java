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
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllCategoryQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetCategoryByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.CategoryCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.CategoryQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers.CategoryAssembler;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.AddThreadToCategoryRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CategoryResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateCategoryRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateCategoryRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Categories", description = "Endpoints for managing Categories")
public class CategoryController {

    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    public CategoryController(CategoryCommandService categoryCommandService, CategoryQueryService categoryQueryService) {
        this.categoryCommandService = categoryCommandService;
        this.categoryQueryService = categoryQueryService;
    }

    @Operation(summary = "Create a new Category", description = "Create a new Category in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CreateCategoryRequest request) {
        var createCategoryCommand = CategoryAssembler.toCommandFromRequest(request);
        var categoryId = this.categoryCommandService.handle(createCategoryCommand);

        if (Objects.isNull(categoryId) || categoryId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getCategoryById = new GetCategoryByIdQuery(categoryId);
        var category = this.categoryQueryService.handle(getCategoryById);

        if (category.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var categoryResponse = CategoryAssembler.toResponseFromEntity(category.get());
        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all Categories", description = "Retrieve a list of all Categories in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Categories found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        var getAllCategoryQuery = new GetAllCategoryQuery();
        var categories = this.categoryQueryService.handle(getAllCategoryQuery);

        var categoryResponses = categories.stream()
                .map(CategoryAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryResponses);
    }

    @Operation(summary = "Get Category by ID", description = "Retrieve a Category by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        var getCategoryByIdQuery = new GetCategoryByIdQuery(id);
        var category = categoryQueryService.handle(getCategoryByIdQuery);

        if (category.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var categoryResponse = CategoryAssembler.toResponseFromEntity(category.get());
        return ResponseEntity.ok(categoryResponse);
    }

    @Operation(summary = "Update Category information", description = "Update the information of an existing Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody UpdateCategoryRequest request) {
        var updateCategoryCommand = CategoryAssembler.toCommandFromRequest(id, request);
        var updatedCategory = this.categoryCommandService.handle(updateCategoryCommand);
        if (updatedCategory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var categoryResponse = CategoryAssembler.toResponseFromEntity(updatedCategory.get());
        return ResponseEntity.ok(categoryResponse);
    }

    @Operation(summary = "Delete Category by ID", description = "Delete a Category by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id) {
        var deleteCategoryCommand = new DeleteCategoryCommand(id);
        this.categoryCommandService.handle(deleteCategoryCommand);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add Thread to Category", description = "Add a Thread to a Category by their unique identifiers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thread added to Category successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category or Thread not found", content = @Content)
    })
    @PostMapping("/{id}/threads")
    public ResponseEntity<CategoryResponse> addThreadToCategory(@PathVariable Long id, @RequestBody AddThreadToCategoryRequest request){
        var command = CategoryAssembler.toCommandFromRequest(id, request);
        this.categoryCommandService.handle(command);
        var category = this.categoryQueryService.handle(new GetCategoryByIdQuery(id));
        if (category.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var categoryResponse = CategoryAssembler.toResponseFromEntity(category.get());
        return ResponseEntity.ok(categoryResponse);
    }
}
