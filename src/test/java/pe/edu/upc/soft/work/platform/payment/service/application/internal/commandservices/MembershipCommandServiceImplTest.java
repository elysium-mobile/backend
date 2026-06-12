package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Membership;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteMembershipCommand;
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
class MembershipCommandServiceImplTest {

    private static final Long MEMBERSHIP_ID = 5L;

    @Mock
    private MembershipRepository membershipRepository;

    @InjectMocks
    private MembershipCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateMembershipCommand) -> creates Membership and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = PaymentCommandFixtures.validCreateMembershipCommand();
        when(membershipRepository.save(any(Membership.class))).thenAnswer(inv -> {
            Membership m = inv.getArgument(0);
            ReflectionTestUtils.setId(m, MEMBERSHIP_ID);
            return m;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(MEMBERSHIP_ID);
        verify(membershipRepository, times(1)).save(any(Membership.class));
        verifyNoMoreInteractions(membershipRepository);
    }

    @Test
    @DisplayName("handle(CreateMembershipCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = PaymentCommandFixtures.validCreateMembershipCommand();
        when(membershipRepository.save(any(Membership.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Membership").contains("db");
        verify(membershipRepository, times(1)).save(any(Membership.class));
        verifyNoMoreInteractions(membershipRepository);
    }

    @Test
    @DisplayName("handle(UpdateMembershipCommand) -> returns Optional with updated Membership when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Membership(PaymentCommandFixtures.validCreateMembershipCommand());
        ReflectionTestUtils.setId(existing, MEMBERSHIP_ID);
        var command = PaymentCommandFixtures.updateMembershipCommand(MEMBERSHIP_ID);
        when(membershipRepository.existsById(MEMBERSHIP_ID)).thenReturn(true);
        when(membershipRepository.findById(MEMBERSHIP_ID)).thenReturn(Optional.of(existing));
        when(membershipRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Membership> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getMembershipStatus()).isEqualTo(PaymentCommandFixtures.VALID_MEMBERSHIP_STATUS);
        verify(membershipRepository, times(1)).existsById(MEMBERSHIP_ID);
        verify(membershipRepository, times(1)).findById(MEMBERSHIP_ID);
        verify(membershipRepository, times(1)).save(existing);
        verifyNoMoreInteractions(membershipRepository);
    }

    @Test
    @DisplayName("handle(UpdateMembershipCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = PaymentCommandFixtures.updateMembershipCommand(MEMBERSHIP_ID);
        when(membershipRepository.existsById(MEMBERSHIP_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(MEMBERSHIP_ID)).contains("does not exist");
        verify(membershipRepository, times(1)).existsById(MEMBERSHIP_ID);
        verifyNoMoreInteractions(membershipRepository);
    }

    @Test
    @DisplayName("handle(UpdateMembershipCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Membership(PaymentCommandFixtures.validCreateMembershipCommand());
        ReflectionTestUtils.setId(existing, MEMBERSHIP_ID);
        var command = PaymentCommandFixtures.updateMembershipCommand(MEMBERSHIP_ID);
        when(membershipRepository.existsById(MEMBERSHIP_ID)).thenReturn(true);
        when(membershipRepository.findById(MEMBERSHIP_ID)).thenReturn(Optional.of(existing));
        when(membershipRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Membership").contains("boom");
        verify(membershipRepository, times(1)).existsById(MEMBERSHIP_ID);
        verify(membershipRepository, times(1)).findById(MEMBERSHIP_ID);
        verify(membershipRepository, times(1)).save(existing);
        verifyNoMoreInteractions(membershipRepository);
    }

    @Test
    @DisplayName("handle(DeleteMembershipCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteMembershipCommand(MEMBERSHIP_ID);
        when(membershipRepository.existsById(MEMBERSHIP_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(membershipRepository, times(1)).existsById(MEMBERSHIP_ID);
        verify(membershipRepository, times(1)).deleteById(MEMBERSHIP_ID);
        verifyNoMoreInteractions(membershipRepository);
    }

    @Test
    @DisplayName("handle(DeleteMembershipCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteMembershipCommand(MEMBERSHIP_ID);
        when(membershipRepository.existsById(MEMBERSHIP_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(MEMBERSHIP_ID)).contains("does not exist");
        verify(membershipRepository, times(1)).existsById(MEMBERSHIP_ID);
        verify(membershipRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(membershipRepository);
    }

    @Test
    @DisplayName("handle(DeleteMembershipCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteMembershipCommand(MEMBERSHIP_ID);
        when(membershipRepository.existsById(MEMBERSHIP_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(membershipRepository).deleteById(MEMBERSHIP_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Membership").contains("fk");
        verify(membershipRepository, times(1)).existsById(MEMBERSHIP_ID);
        verify(membershipRepository, times(1)).deleteById(MEMBERSHIP_ID);
        verifyNoMoreInteractions(membershipRepository);
    }
}
