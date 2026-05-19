package pe.edu.upc.soft.work.platform.shared.interfaces.rest.resources;

public record ServiceUnavailableResponse(
        int status, String error, String message
) {
}
