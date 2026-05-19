package pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationDetailResponse(


        Long notificationDetailId,

        @JsonProperty("title")
        String title,

        @JsonProperty("content")
        String content
) {
}
