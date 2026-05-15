package pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects.NotificationType;

public record CreateNotificationRequest(


        @NotNull
        @NotBlank
        @JsonProperty("seen")
        boolean seen,

        @NotNull
        @NotBlank
        @JsonProperty("notification_type")
        String notificationType,

        @NotNull
        @NotBlank
        @JsonProperty("user_account_id")
        Long userAccountId

) {
}
