package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Benefit;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteBenefitCommand;

import java.util.Optional;

/**
 * Service interface for handling Benefit-related commands.
 */
public interface BenefitCommandService {

    /**
     * Handles the creation of a new Benefit.
     */
    Long handle(CreateBenefitCommand command);

    /**
     * Handles the update of an existing Benefit.
     */
    Optional<Benefit> handle(UpdateBenefitCommand command);

    /**
     * Handles the deletion of an existing Benefit.
     */
    void handle(DeleteBenefitCommand command);
}
