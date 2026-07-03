package pe.edu.upc.soft.work.platform.shared.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.util.Map;

/**
 * Bad request response record.
 *
 * @param status the HTTP status code
 * @param error the error description
 * @param message the error message
 * @param fieldErrors the map of field-specific errors
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record BadRequestResponse(
        int status, String error, String message, Map<String, String> fieldErrors
) {
}
