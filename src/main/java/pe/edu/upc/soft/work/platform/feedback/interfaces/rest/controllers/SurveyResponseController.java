package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllSurveyResponseQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyResponseByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyResponseCommandService;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyResponseQueryService;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers.SurveyResponseAssembler;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.CreateSurveyResponseRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.SurveyResponseResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/survey-responses", produces = "application/json")
@Tag(name = "Survey-Responses", description = "Endpoints for managing Survey-Responses")
public class SurveyResponseController {

    private final SurveyResponseCommandService surveyResponseCommandService;
    private final SurveyResponseQueryService surveyResponseQueryService;

    public SurveyResponseController(SurveyResponseCommandService surveyResponseCommandService, SurveyResponseQueryService surveyResponseQueryService){
        this.surveyResponseCommandService = surveyResponseCommandService;
        this.surveyResponseQueryService = surveyResponseQueryService;
    }


    @Operation(summary = "Create a new Survey-Response", description = "Create a new Survey-Response in the system")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Survey-Response created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Survey-Response not found")
    })
    @GetMapping
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
        return ResponseEntity.ok(surveyResponses);
    }

    public ResponseEntity<SurveyResponseResponse> getSurveyResponseById
}
