package pe.edu.upc.soft.work.platform.shared.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ServiceUnavailableResponse(
        int status, String error, String message
) {
}
