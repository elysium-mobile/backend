package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllSurveyResponseQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyResponseByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyResponseCommandService;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyResponseQueryService;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers.SurveyResponseAssembler;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.CreateSurveyResponseRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.SurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.SurveyResponseResponse;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.UpdateSurveyResponseRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/survey-responses", produces = "application/json")
@Tag(name = "Survey Responses", description = "Endpoints for managing Survey-Responses")
public class SurveyResponseController {

    private final SurveyResponseCommandService surveyResponseCommandService;
    private final SurveyResponseQueryService surveyResponseQueryService;

    public SurveyResponseController(SurveyResponseCommandService surveyResponseCommandService, SurveyResponseQueryService surveyResponseQueryService){
        this.surveyResponseCommandService = surveyResponseCommandService;
        this.surveyResponseQueryService = surveyResponseQueryService;
    }


    @Operation(summary = "Create a new Survey-Response", description = "Create a new Survey-Response in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Survey-Response created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Survey-Response not found")
    })
    @PostMapping
    public ResponseEntity<SurveyResponseResponse> createSurveyResponse(@RequestBody CreateSurveyResponseRequest request){
        var createSurveyResponseCommand = SurveyResponseAssembler.toCommandFromRequest(request);
        var responseId= this.surveyResponseCommandService.handle(createSurveyResponseCommand);

        if (Objects.isNull(responseId) || responseId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getSurveyResponseById = new GetSurveyResponseByIdQuery(responseId);
        var surveyResponse = this.surveyResponseQueryService.handle(getSurveyResponseById);

        if (surveyResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var surveyResponseResponse = SurveyResponseAssembler.toResponseFromEntity(surveyResponse.get());
        return ResponseEntity.ok(surveyResponseResponse);
    }


    @Operation(summary = "Get all Survey-Responses", description = "Retrieve a list of all Survey-Responses in the system")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Survey-Responses retrieved successfully")
    )
    @GetMapping
    public ResponseEntity<List<SurveyResponseResponse>> getAllSurveyResponses() {
        var getAllSurveyResponseQuery = new GetAllSurveyResponseQuery();
        var surveyResponses = this.surveyResponseQueryService.handle(getAllSurveyResponseQuery);
        var surveyResponseResponses = surveyResponses.stream()
                .map(SurveyResponseAssembler::toResponseFromEntity).
                collect(Collectors.toList());
        return ResponseEntity.ok(surveyResponseResponses);
    }

    @Operation(summary = "Get Survey-Responses by Survey ID", description = "Retrieve a list of Survey-Responses associated with a specific Survey ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Survey-Responses retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Survey not found")
    }
    )
    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<List<SurveyResponseResponse>> getSurveyResponsesBySurveyId(@PathVariable Long surveyId) {
        var getAllSurveyResponseQuery = new GetAllSurveyResponseQuery();
        var surveyResponses = this.surveyResponseQueryService.handle(getAllSurveyResponseQuery);
        var surveyResponseResponses = surveyResponses.stream()
                .filter(surveyResponse -> surveyResponse.getSurveyId().equals(surveyId))
                .map(SurveyResponseAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(surveyResponseResponses);
    }

    @Operation(summary = "Update Survey Response information", description = "Update the information of an existing Survey Response")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Survey Response updated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = SurveyResponseResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Survey Response not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SurveyResponseResponse> updateSurveyResponse(@PathVariable Long id, @RequestBody UpdateSurveyResponseRequest request){
        var updateSurveyResponseCommand= SurveyResponseAssembler.toCommandFromRequest(id,request);
        var updatedSurveyResponse = this.surveyResponseCommandService.handle(updateSurveyResponseCommand);

        if (updatedSurveyResponse.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var surveyResponseResponse = SurveyResponseAssembler.toResponseFromEntity(updatedSurveyResponse.get());
        return ResponseEntity.ok(surveyResponseResponse);
    }

    @Operation(summary = "Delete Survey Response by ID", description = "Delete a Survey Response by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Survey Response deleted successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "Survey Response not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSurveyResponseById(@PathVariable Long id){
        var deleteSurveyResponseCommand = new DeleteSurveyResponseCommand(id);
        this.surveyResponseCommandService.handle(deleteSurveyResponseCommand);
        return ResponseEntity.noContent().build();
    }

}
