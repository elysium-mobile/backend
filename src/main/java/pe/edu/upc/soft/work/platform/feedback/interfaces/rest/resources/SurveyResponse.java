package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.TargetType;

import java.util.Date;

/**
 * Response object representing a Survey in the system.
 */
public record SurveyResponse(
        Long surveyId,
        String title,
        String description,
        String targetType,
        Date expirationTime
) {}
