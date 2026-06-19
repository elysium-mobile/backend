package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.MembershipPlan;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.BenefitRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipPlanRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipRepository;
import pe.edu.upc.soft.work.platform.payment.service.test.fixtures.PaymentCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MembershipPlanCommandServiceImplTest {

    private static final Long PLAN_ID = 31L;

    @Mock
    private MembershipPlanRepository membershipplanRepository;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private BenefitRepository benefitRepository;


    @InjectMocks
    private MembershipPlanCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateMembershipPlanCommand) -> creates MembershipPlan and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = PaymentCommandFixtures.validCreateMembershipPlanCommand();
        when(membershipRepository.existsById(command.membershipId())).thenReturn(true);
        when(membershipplanRepository.save(any(MembershipPlan.class))).thenAnswer(inv -> {
            MembershipPlan p = inv.getArgument(0);
            ReflectionTestUtils.setId(p, PLAN_ID);
            return p;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(PLAN_ID);
        verify(membershipRepository).existsById(command.membershipId());
        verify(membershipplanRepository).save(any(MembershipPlan.class));
    }

    @Test
    @DisplayName("handle(CreateMembershipPlanCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = PaymentCommandFixtures.validCreateMembershipPlanCommand();
        when(membershipRepository.existsById(command.membershipId())).thenReturn(true);
        when(membershipplanRepository.save(any(MembershipPlan.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating MembershipPlan").contains("db");
        verify(membershipRepository, times(1)).existsById(command.membershipId());
        verify(membershipplanRepository, times(1)).save(any(MembershipPlan.class));
        verifyNoMoreInteractions(membershipRepository, membershipplanRepository);
    }

    @Test
    @DisplayName("handle(UpdateMembershipPlanCommand) -> returns Optional with updated MembershipPlan when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new MembershipPlan(PaymentCommandFixtures.validCreateMembershipPlanCommand());
        ReflectionTestUtils.setId(existing, PLAN_ID);
        var command = PaymentCommandFixtures.updateMembershipPlanCommand(PLAN_ID);
        when(membershipRepository.existsById(command.membershipId())).thenReturn(true);
        when(membershipplanRepository.existsById(PLAN_ID)).thenReturn(true);
        when(membershipplanRepository.findById(PLAN_ID)).thenReturn(Optional.of(existing));
        when(membershipplanRepository.save(any(MembershipPlan.class))).thenReturn(existing);

        // Act
        Optional<MembershipPlan> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        verify(membershipRepository).existsById(command.membershipId());
        verify(membershipplanRepository).existsById(PLAN_ID);
        verify(membershipplanRepository).save(any(MembershipPlan.class));
    }

    @Test
    @DisplayName("handle(UpdateMembershipPlanCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = PaymentCommandFixtures.updateMembershipPlanCommand(PLAN_ID);
        when(membershipRepository.existsById(command.membershipId())).thenReturn(true);
        when(membershipplanRepository.existsById(PLAN_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(PLAN_ID)).contains("does not exist");
        verify(membershipRepository, times(1)).existsById(command.membershipId());
        verify(membershipplanRepository, times(1)).existsById(PLAN_ID);
        verifyNoMoreInteractions(membershipRepository, membershipplanRepository);
    }

    @Test
    @DisplayName("handle(UpdateMembershipPlanCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new MembershipPlan(PaymentCommandFixtures.validCreateMembershipPlanCommand());
        ReflectionTestUtils.setId(existing, PLAN_ID);
        var command = PaymentCommandFixtures.updateMembershipPlanCommand(PLAN_ID);
        when(membershipRepository.existsById(command.membershipId())).thenReturn(true);
        when(membershipplanRepository.existsById(PLAN_ID)).thenReturn(true);
        when(membershipplanRepository.findById(PLAN_ID)).thenReturn(Optional.of(existing));
        when(membershipplanRepository.save(any(MembershipPlan.class))).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating MembershipPlan").contains("boom");
        verify(membershipRepository, times(1)).existsById(command.membershipId());
        verify(membershipplanRepository, times(1)).existsById(PLAN_ID);
        verify(membershipplanRepository, times(1)).findById(PLAN_ID);
        verify(membershipplanRepository, times(1)).save(any(MembershipPlan.class));
        verifyNoMoreInteractions(membershipRepository, membershipplanRepository);
    }

    @Test
    @DisplayName("handle(DeleteMembershipPlanCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteMembershipPlanCommand(PLAN_ID);
        when(membershipplanRepository.existsById(PLAN_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(membershipplanRepository, times(1)).existsById(PLAN_ID);
        verify(membershipplanRepository, times(1)).deleteById(PLAN_ID);
        verifyNoMoreInteractions(membershipplanRepository);
    }

    @Test
    @DisplayName("handle(DeleteMembershipPlanCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteMembershipPlanCommand(PLAN_ID);
        when(membershipplanRepository.existsById(PLAN_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(PLAN_ID)).contains("does not exist");
        verify(membershipplanRepository, times(1)).existsById(PLAN_ID);
        verify(membershipplanRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(membershipplanRepository);
    }

    @Test
    @DisplayName("handle(DeleteMembershipPlanCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteMembershipPlanCommand(PLAN_ID);
        when(membershipplanRepository.existsById(PLAN_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(membershipplanRepository).deleteById(PLAN_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting MembershipPlan").contains("fk");
        verify(membershipplanRepository, times(1)).existsById(PLAN_ID);
        verify(membershipplanRepository, times(1)).deleteById(PLAN_ID);
        verifyNoMoreInteractions(membershipplanRepository);
    }
}
