package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.MembershipPlan;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreateMembershipPlanRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.MembershipPlanResponse;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.UpdateMembershipPlanRequest;
import pe.edu.upc.soft.work.platform.payment.service.test.fixtures.PaymentCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipPlanAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateMembershipPlanRequest) -> maps planName and price to CreateMembershipPlanCommand (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateMembershipPlanRequest(
                PaymentCommandFixtures.VALID_PLAN_NAME,
                PaymentCommandFixtures.VALID_PLAN_PRICE);

        // Act
        CreateMembershipPlanCommand command = MembershipPlanAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.planName()).isEqualTo(PaymentCommandFixtures.VALID_PLAN_NAME);
        assertThat(command.price()).isEqualTo(PaymentCommandFixtures.VALID_PLAN_PRICE);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateMembershipPlanRequest) -> maps id, planName and price (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateMembershipPlanRequest(
                PaymentCommandFixtures.VALID_PLAN_NAME,
                PaymentCommandFixtures.VALID_PLAN_PRICE);

        // Act
        UpdateMembershipPlanCommand command = MembershipPlanAssembler.toCommandFromRequest(31L, request);

        // Assert
        assertThat(command.membershipplanId()).isEqualTo(31L);
        assertThat(command.planName()).isEqualTo(PaymentCommandFixtures.VALID_PLAN_NAME);
        assertThat(command.price()).isEqualTo(PaymentCommandFixtures.VALID_PLAN_PRICE);
    }

    @Test
    @DisplayName("toResponseFromEntity(MembershipPlan) -> maps every field to MembershipPlanResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new MembershipPlan(PaymentCommandFixtures.validCreateMembershipPlanCommand());
        ReflectionTestUtils.setId(entity, 31L);

        // Act
        MembershipPlanResponse response = MembershipPlanAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.planId()).isEqualTo(31L);
        assertThat(response.planName()).isEqualTo(PaymentCommandFixtures.VALID_PLAN_NAME);
        assertThat(response.price()).isEqualTo(PaymentCommandFixtures.VALID_PLAN_PRICE);
    }
}
