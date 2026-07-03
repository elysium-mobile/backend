package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * Request object for updating an existing Thread.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateThreadRequest(
        @NotNull
        @NotBlank
        String title,
        @NotNull
        @NotBlank
        Long areaCompanyId,
        @NotNull
        @NotBlank
        Date lastMessage,
        @NotNull
        @NotBlank
        Long categoryId,
        @NotNull
        @NotBlank
        Integer messageCount
) {}
