package pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects.NotificationType;

public record CreateNotificationDetailRequest(

        @NotNull
        @NotBlank
        @JsonProperty("title")
        String title,

        @NotNull
        @NotBlank
        @JsonProperty("content")
        String content,

        @NotNull
        @NotBlank
        @JsonProperty("notificationId")
        Long notificationId

        ) {
}
