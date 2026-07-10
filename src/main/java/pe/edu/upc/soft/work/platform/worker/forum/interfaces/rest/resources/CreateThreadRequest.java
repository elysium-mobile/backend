package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * Request object for creating a new Thread.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateThreadRequest(
        @NotNull
        @NotBlank
        String title,
        @NotNull
        Long areaCompanyId,
        @NotNull
        Date lastMessage,
        @NotNull
        Long categoryId,
        @NotNull
        Integer messageCount
) {}
