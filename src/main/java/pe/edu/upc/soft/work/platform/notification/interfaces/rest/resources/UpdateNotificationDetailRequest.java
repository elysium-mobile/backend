package pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateNotificationDetailRequest(
        @NotNull
        @NotBlank
        String title,

        @NotNull
        @NotBlank
        String content,

        @NotNull
        Long notificationId

) {
}
