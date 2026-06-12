package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record AddUnitOfWorkToAreaCompanyRequest(
        @NotNull
        @JsonProperty("unitOfWorkId")
        Long unitOfWorkId
) {
}
