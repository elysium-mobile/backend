package pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects.NotificationType;

public record NotificationResponse(

        Long notificationId,
        @JsonProperty("seen")
        boolean seen,

        @JsonProperty("notification_type")
        NotificationType notificationType,

        @JsonProperty("user_account_id")
        Long userAccountId
) {
}
