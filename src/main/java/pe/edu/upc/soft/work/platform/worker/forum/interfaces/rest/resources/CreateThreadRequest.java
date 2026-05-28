package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * Request object for creating a new Thread.
 */
public record CreateThreadRequest(
        @NotNull
        @NotBlank
        String title,
        @NotNull
        @NotBlank
        Long areaCompanyId,
        @NotNull
        @NotBlank
        Date lastMessage
) {}
