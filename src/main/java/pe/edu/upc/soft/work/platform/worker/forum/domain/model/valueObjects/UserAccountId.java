package pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserAccountId(Long userAccountId) {

    public UserAccountId{
        if(userAccountId == null)
        {
            throw new IllegalArgumentException("[userAccountId] must not be null");
        }
    }
}
