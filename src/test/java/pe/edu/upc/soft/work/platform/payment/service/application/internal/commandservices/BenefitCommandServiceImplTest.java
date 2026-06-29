package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Benefit;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.MembershipPlan;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.BenefitRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipPlanRepository;
import pe.edu.upc.soft.work.platform.payment.service.test.fixtures.PaymentCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BenefitCommandServiceImplTest {

    private static final Long BENEFIT_ID = 11L;

    @Mock
    private BenefitRepository benefitRepository;

    @Mock
    private MembershipPlanRepository membershipPlanRepository;

    @InjectMocks
    private BenefitCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateBenefitCommand) -> creates Benefit and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = PaymentCommandFixtures.validCreateBenefitCommand();
        var membershipPlan = new MembershipPlan();
        when(membershipPlanRepository.existsById(command.membershipPlanId())).thenReturn(true);
        when(membershipPlanRepository.findById(command.membershipPlanId())).thenReturn(Optional.of(membershipPlan));
        when(benefitRepository.save(any(Benefit.class))).thenAnswer(inv -> {
            Benefit b = inv.getArgument(0);
            ReflectionTestUtils.setId(b, BENEFIT_ID);
            return b;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(BENEFIT_ID);
        verify(membershipPlanRepository, times(1)).existsById(command.membershipPlanId());
        verify(membershipPlanRepository, times(1)).findById(command.membershipPlanId());
        verify(benefitRepository, times(1)).save(any(Benefit.class));
        verify(membershipPlanRepository, times(1)).save(any(MembershipPlan.class));
        verifyNoMoreInteractions(membershipPlanRepository, benefitRepository);
    }

    @Test
    @DisplayName("handle(CreateBenefitCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = PaymentCommandFixtures.validCreateBenefitCommand();
        var membershipPlan = new MembershipPlan();
        when(membershipPlanRepository.existsById(command.membershipPlanId())).thenReturn(true);
        when(membershipPlanRepository.findById(command.membershipPlanId())).thenReturn(Optional.of(membershipPlan));
        when(benefitRepository.save(any(Benefit.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Benefit").contains("db");
        verify(membershipPlanRepository).existsById(command.membershipPlanId());
        verify(membershipPlanRepository).findById(command.membershipPlanId());
        verify(benefitRepository).save(any(Benefit.class));
    }

    @Test
    @DisplayName("handle(UpdateBenefitCommand) -> returns Optional with updated Benefit when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Benefit(PaymentCommandFixtures.validCreateBenefitCommand());
        ReflectionTestUtils.setId(existing, BENEFIT_ID);
        var command = PaymentCommandFixtures.updateBenefitCommand(BENEFIT_ID);

        when(membershipPlanRepository.existsById(command.membershipPlanId())).thenReturn(true);
        when(benefitRepository.existsById(BENEFIT_ID)).thenReturn(true);
        when(benefitRepository.findById(BENEFIT_ID)).thenReturn(Optional.of(existing));
        when(benefitRepository.save(any(Benefit.class))).thenReturn(existing);

        // Act
        Optional<Benefit> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(PaymentCommandFixtures.VALID_BENEFIT_TITLE);
        verify(membershipPlanRepository, times(1)).existsById(command.membershipPlanId());
        verify(benefitRepository, times(1)).existsById(BENEFIT_ID);
        verify(benefitRepository, times(1)).findById(BENEFIT_ID);
        verify(benefitRepository, times(1)).save(any(Benefit.class));
        verifyNoMoreInteractions(membershipPlanRepository, benefitRepository);
    }

    @Test
    @DisplayName("handle(UpdateBenefitCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = PaymentCommandFixtures.updateBenefitCommand(BENEFIT_ID);
        when(membershipPlanRepository.existsById(command.membershipPlanId())).thenReturn(true);
        when(benefitRepository.existsById(BENEFIT_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(BENEFIT_ID)).contains("does not exist");
        verify(membershipPlanRepository).existsById(command.membershipPlanId());
        verify(benefitRepository).existsById(BENEFIT_ID);
    }

    @Test
    @DisplayName("handle(UpdateBenefitCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Benefit(PaymentCommandFixtures.validCreateBenefitCommand());
        ReflectionTestUtils.setId(existing, BENEFIT_ID);
        var command = PaymentCommandFixtures.updateBenefitCommand(BENEFIT_ID);
        when(membershipPlanRepository.existsById(command.membershipPlanId())).thenReturn(true);
        when(benefitRepository.existsById(BENEFIT_ID)).thenReturn(true);
        when(benefitRepository.findById(BENEFIT_ID)).thenReturn(Optional.of(existing));
        when(benefitRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Benefit").contains("boom");
        verify(membershipPlanRepository, times(1)).existsById(command.membershipPlanId());
        verify(benefitRepository, times(1)).existsById(BENEFIT_ID);
        verify(benefitRepository, times(1)).findById(BENEFIT_ID);
        verify(benefitRepository, times(1)).save(existing);
        verifyNoMoreInteractions(benefitRepository, membershipPlanRepository);
    }

    @Test
    @DisplayName("handle(DeleteBenefitCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteBenefitCommand(BENEFIT_ID);

        var benefit = new Benefit();
        ReflectionTestUtils.setId(benefit, BENEFIT_ID);
        benefit.setMembershipPlanId(1L);

        var membershipPlan = new MembershipPlan();
        membershipPlan.addBenefit(benefit);

        when(benefitRepository.findById(BENEFIT_ID)).thenReturn(Optional.of(benefit));
        when(membershipPlanRepository.findById(1L)).thenReturn(Optional.of(membershipPlan));

        // Act
        service.handle(command);

        // Assert
        verify(membershipPlanRepository, times(1)).save(any(MembershipPlan.class));
        verify(benefitRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(benefitRepository, membershipPlanRepository);
    }

    @Test
    @DisplayName("handle(DeleteBenefitCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteBenefitCommand(BENEFIT_ID);
        when(benefitRepository.findById(BENEFIT_ID)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(BENEFIT_ID)).contains("does not exist");
        verify(benefitRepository, times(1)).findById(BENEFIT_ID);
        verify(membershipPlanRepository, never()).findById(anyLong());
        verifyNoMoreInteractions(benefitRepository, membershipPlanRepository);
    }

    @Test
    @DisplayName("handle(DeleteBenefitCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteBenefitCommand(BENEFIT_ID);
        var benefit = new Benefit();
        ReflectionTestUtils.setId(benefit, BENEFIT_ID);
        benefit.setMembershipPlanId(1L);

        var membershipPlan = new MembershipPlan();
        membershipPlan.addBenefit(benefit);
        when(benefitRepository.findById(BENEFIT_ID)).thenReturn(Optional.of(benefit));
        when(membershipPlanRepository.findById(1L)).thenReturn(Optional.of(membershipPlan));
        doThrow(new RuntimeException("fk")).when(membershipPlanRepository).save(any(MembershipPlan.class));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Benefit").contains("fk");
        verify(membershipPlanRepository, times(1)).save(any(MembershipPlan.class));
    }
}
