package pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing the identifier of a company.
 * @param CompanyId the identifier of the company

**/
@Embeddable
public record CompanyId(Long CompanyId) {

    /**
     * Constructor with validation.
     * @param CompanyId the identifier of the company
     */
    public CompanyId {
        if (CompanyId == null) {
            throw new IllegalArgumentException("[CompanyId] CompanyId must not be null");
        }
    }
}
