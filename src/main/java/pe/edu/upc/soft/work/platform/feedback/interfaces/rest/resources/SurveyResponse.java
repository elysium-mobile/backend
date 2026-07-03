package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.util.Date;

/**
 * Response object representing a Survey in the system.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SurveyResponse(
        Long surveyId,
        String title,
        String description,
        String targetType,
        Date expirationTime
) {}
