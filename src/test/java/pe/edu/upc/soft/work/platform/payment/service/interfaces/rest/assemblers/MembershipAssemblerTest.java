package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Membership;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreateMembershipRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.MembershipResponse;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.UpdateMembershipRequest;
import pe.edu.upc.soft.work.platform.payment.service.test.fixtures.PaymentCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateMembershipRequest) -> maps fields and resolves MembershipStatus enum (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateMembershipRequest(
                PaymentCommandFixtures.VALID_MEMBERSHIP_START,
                PaymentCommandFixtures.VALID_MEMBERSHIP_OVER,
                PaymentCommandFixtures.VALID_MEMBERSHIP_STATUS.name());

        // Act
        CreateMembershipCommand command = MembershipAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.membershipStart()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_START);
        assertThat(command.membershipOver()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_OVER);
        assertThat(command.membershipStatus()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_STATUS);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateMembershipRequest) -> maps id and fields to UpdateMembershipCommand (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateMembershipRequest(
                PaymentCommandFixtures.VALID_MEMBERSHIP_START,
                PaymentCommandFixtures.VALID_MEMBERSHIP_OVER,
                PaymentCommandFixtures.VALID_MEMBERSHIP_STATUS.name());

        // Act
        UpdateMembershipCommand command = MembershipAssembler.toCommandFromRequest(5L, request);

        // Assert
        assertThat(command.membershipId()).isEqualTo(5L);
        assertThat(command.membershipStart()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_START);
        assertThat(command.membershipOver()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_OVER);
        assertThat(command.membershipStatus()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_STATUS);
    }

    @Test
    @DisplayName("toResponseFromEntity(Membership) -> maps every field and serializes MembershipStatus as name() (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var membership = new Membership(PaymentCommandFixtures.validCreateMembershipCommand());
        ReflectionTestUtils.setId(membership, 5L);

        // Act
        MembershipResponse response = MembershipAssembler.toResponseFromEntity(membership);

        // Assert
        assertThat(response.membershipId()).isEqualTo(5L);
        assertThat(response.membershipStart()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_START);
        assertThat(response.membershipOver()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_OVER);
        assertThat(response.membershipStatus()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_STATUS.name());
    }
}
