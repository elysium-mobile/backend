package pe.edu.upc.soft.work.platform.dashboard.domain.model.valueObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record MessageId(Long messageId) {

    public MessageId {
        if (messageId ==null)
        {
            throw new IllegalArgumentException("[messageId] must no be null");
        }
    }
}
