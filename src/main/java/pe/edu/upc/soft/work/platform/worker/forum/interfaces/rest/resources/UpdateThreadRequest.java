package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * Request object for updating an existing Thread.
 */
public record UpdateThreadRequest(
        @NotNull
        @NotBlank
        String title,
        @NotNull
        @NotBlank
        @JsonProperty("areaCompnayId")
        Long areaCompanyId,
        @NotNull
        @NotBlank
        @JsonProperty("lastMessage")
        Date lastMessage
) {}
