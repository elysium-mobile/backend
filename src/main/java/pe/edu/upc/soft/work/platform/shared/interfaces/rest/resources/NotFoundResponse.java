package pe.edu.upc.soft.work.platform.shared.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

/**
 * Not Found Response DTO.
 *
 * @param status the HTTP status code
 * @param error the error reason phrase
 * @param message the error message
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NotFoundResponse(
        int status, String error, String message
) {
}
