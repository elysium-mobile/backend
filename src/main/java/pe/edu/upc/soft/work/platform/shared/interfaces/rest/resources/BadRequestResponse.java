package pe.edu.upc.soft.work.platform.shared.interfaces.rest.resources;

import java.util.Map;

/**
 * Bad request response record.
 *
 * @param status the HTTP status code
 * @param error the error description
 * @param message the error message
 * @param fieldErrors the map of field-specific errors
 */
public record BadRequestResponse(
        int status, String error, String message, Map<String, String> fieldErrors
) {
}
