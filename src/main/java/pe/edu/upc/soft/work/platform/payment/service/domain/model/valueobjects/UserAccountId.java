package pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserAccountId(Long userAccountId) {

    public UserAccountId{
        if(userAccountId == null){
            throw new IllegalArgumentException("[userAccountId] must no be null");
        }
    }
}
