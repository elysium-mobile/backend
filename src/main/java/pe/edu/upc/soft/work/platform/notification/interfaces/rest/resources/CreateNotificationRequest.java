package pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects.NotificationType;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateNotificationRequest(


        @NotNull
        @NotBlank
        boolean seen,

        @NotNull
        @NotBlank
        String notificationType,

        @NotNull
        @NotBlank
        Long userAccountId


        ) {
}
