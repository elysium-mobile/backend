package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Benefit;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.BenefitRepository;
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
class BenefitCommandServiceImplTest {

    private static final Long BENEFIT_ID = 11L;

    @Mock
    private BenefitRepository benefitRepository;

    @InjectMocks
    private BenefitCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateBenefitCommand) -> creates Benefit and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = PaymentCommandFixtures.validCreateBenefitCommand();
        when(benefitRepository.save(any(Benefit.class))).thenAnswer(inv -> {
            Benefit b = inv.getArgument(0);
            ReflectionTestUtils.setId(b, BENEFIT_ID);
            return b;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(BENEFIT_ID);
        verify(benefitRepository, times(1)).save(any(Benefit.class));
        verifyNoMoreInteractions(benefitRepository);
    }

    @Test
    @DisplayName("handle(CreateBenefitCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = PaymentCommandFixtures.validCreateBenefitCommand();
        when(benefitRepository.save(any(Benefit.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Benefit").contains("db");
        verify(benefitRepository, times(1)).save(any(Benefit.class));
        verifyNoMoreInteractions(benefitRepository);
    }

    @Test
    @DisplayName("handle(UpdateBenefitCommand) -> returns Optional with updated Benefit when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Benefit(PaymentCommandFixtures.validCreateBenefitCommand());
        ReflectionTestUtils.setId(existing, BENEFIT_ID);
        var command = PaymentCommandFixtures.updateBenefitCommand(BENEFIT_ID);
        when(benefitRepository.existsById(BENEFIT_ID)).thenReturn(true);
        when(benefitRepository.findById(BENEFIT_ID)).thenReturn(Optional.of(existing));
        when(benefitRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Benefit> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(PaymentCommandFixtures.VALID_BENEFIT_TITLE);
        assertThat(result.get().getDescription()).isEqualTo(PaymentCommandFixtures.VALID_BENEFIT_DESCRIPTION);
        verify(benefitRepository, times(1)).existsById(BENEFIT_ID);
        verify(benefitRepository, times(1)).findById(BENEFIT_ID);
        verify(benefitRepository, times(1)).save(existing);
        verifyNoMoreInteractions(benefitRepository);
    }

    @Test
    @DisplayName("handle(UpdateBenefitCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = PaymentCommandFixtures.updateBenefitCommand(BENEFIT_ID);
        when(benefitRepository.existsById(BENEFIT_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(BENEFIT_ID)).contains("does not exist");
        verify(benefitRepository, times(1)).existsById(BENEFIT_ID);
        verifyNoMoreInteractions(benefitRepository);
    }

    @Test
    @DisplayName("handle(UpdateBenefitCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Benefit(PaymentCommandFixtures.validCreateBenefitCommand());
        ReflectionTestUtils.setId(existing, BENEFIT_ID);
        var command = PaymentCommandFixtures.updateBenefitCommand(BENEFIT_ID);
        when(benefitRepository.existsById(BENEFIT_ID)).thenReturn(true);
        when(benefitRepository.findById(BENEFIT_ID)).thenReturn(Optional.of(existing));
        when(benefitRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Benefit").contains("boom");
        verify(benefitRepository, times(1)).existsById(BENEFIT_ID);
        verify(benefitRepository, times(1)).findById(BENEFIT_ID);
        verify(benefitRepository, times(1)).save(existing);
        verifyNoMoreInteractions(benefitRepository);
    }

    @Test
    @DisplayName("handle(DeleteBenefitCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteBenefitCommand(BENEFIT_ID);
        when(benefitRepository.existsById(BENEFIT_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(benefitRepository, times(1)).existsById(BENEFIT_ID);
        verify(benefitRepository, times(1)).deleteById(BENEFIT_ID);
        verifyNoMoreInteractions(benefitRepository);
    }

    @Test
    @DisplayName("handle(DeleteBenefitCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteBenefitCommand(BENEFIT_ID);
        when(benefitRepository.existsById(BENEFIT_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(BENEFIT_ID)).contains("does not exist");
        verify(benefitRepository, times(1)).existsById(BENEFIT_ID);
        verify(benefitRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(benefitRepository);
    }

    @Test
    @DisplayName("handle(DeleteBenefitCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteBenefitCommand(BENEFIT_ID);
        when(benefitRepository.existsById(BENEFIT_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(benefitRepository).deleteById(BENEFIT_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Benefit").contains("fk");
        verify(benefitRepository, times(1)).existsById(BENEFIT_ID);
        verify(benefitRepository, times(1)).deleteById(BENEFIT_ID);
        verifyNoMoreInteractions(benefitRepository);
    }
}
