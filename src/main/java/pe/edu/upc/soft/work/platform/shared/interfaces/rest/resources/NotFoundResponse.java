package pe.edu.upc.soft.work.platform.shared.interfaces.rest.resources;

/**
 * Not Found Response DTO.
 *
 * @param status the HTTP status code
 * @param error the error reason phrase
 * @param message the error message
 */
public record NotFoundResponse(
        int status, String error, String message
) {
}
