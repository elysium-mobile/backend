package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Benefit;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.BenefitResponse;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreateBenefitRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.UpdateBenefitRequest;
import pe.edu.upc.soft.work.platform.payment.service.test.fixtures.PaymentCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class BenefitAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateBenefitRequest) -> maps title and description to CreateBenefitCommand (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateBenefitRequest(
                PaymentCommandFixtures.VALID_BENEFIT_TITLE,
                PaymentCommandFixtures.VALID_BENEFIT_DESCRIPTION,
            PaymentCommandFixtures.VALID_MEMBERSHIP_PLAN_ID);

        // Act
        CreateBenefitCommand command = BenefitAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.title()).isEqualTo(PaymentCommandFixtures.VALID_BENEFIT_TITLE);
        assertThat(command.description()).isEqualTo(PaymentCommandFixtures.VALID_BENEFIT_DESCRIPTION);
        assertThat(command.membershipPlanId()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_PLAN_ID);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateBenefitRequest) -> maps id, title and description (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateBenefitRequest(
                PaymentCommandFixtures.VALID_BENEFIT_TITLE,
                PaymentCommandFixtures.VALID_BENEFIT_DESCRIPTION,
            PaymentCommandFixtures.VALID_MEMBERSHIP_PLAN_ID);

        // Act
        UpdateBenefitCommand command = BenefitAssembler.toCommandFromRequest(11L, request);

        // Assert
        assertThat(command.benefitId()).isEqualTo(11L);
        assertThat(command.title()).isEqualTo(PaymentCommandFixtures.VALID_BENEFIT_TITLE);
        assertThat(command.description()).isEqualTo(PaymentCommandFixtures.VALID_BENEFIT_DESCRIPTION);
        assertThat(command.membershipPlanId()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_PLAN_ID);
    }

    @Test
    @DisplayName("toResponseFromEntity(Benefit) -> maps id, title and description to BenefitResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new Benefit(PaymentCommandFixtures.validCreateBenefitCommand());
        ReflectionTestUtils.setId(entity, 11L);

        // Act
        BenefitResponse response = BenefitAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.benefitId()).isEqualTo(11L);
        assertThat(response.title()).isEqualTo(PaymentCommandFixtures.VALID_BENEFIT_TITLE);
        assertThat(response.description()).isEqualTo(PaymentCommandFixtures.VALID_BENEFIT_DESCRIPTION);
        assertThat(response.membershipPlanId()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_PLAN_ID);
    }
}
