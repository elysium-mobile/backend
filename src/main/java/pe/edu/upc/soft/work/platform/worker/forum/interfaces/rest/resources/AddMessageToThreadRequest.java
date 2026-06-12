package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record AddMessageToThreadRequest(
    @NotNull
    @JsonProperty("messageId")
    Long messageId
){

}
