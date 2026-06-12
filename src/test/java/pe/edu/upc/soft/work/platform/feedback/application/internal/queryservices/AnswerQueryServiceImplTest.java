package pe.edu.upc.soft.work.platform.feedback.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.Answer;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllAnswerQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAnswerByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.AnswerRepository;
import pe.edu.upc.soft.work.platform.feedback.test.fixtures.FeedbackCommandFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnswerQueryServiceImplTest {

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private AnswerQueryServiceImpl service;

    private static Answer sample() {
        return new Answer(FeedbackCommandFixtures.validCreateAnswerCommand());
    }

    @Test
    @DisplayName("handle(GetAllAnswerQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Answer> answers = List.of(sample());
        when(answerRepository.findAll()).thenReturn(answers);

        // Act
        List<Answer> result = service.handle(new GetAllAnswerQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(answers);
        verify(answerRepository, times(1)).findAll();
        verifyNoMoreInteractions(answerRepository);
    }

    @Test
    @DisplayName("handle(GetAllAnswerQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(answerRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Answer> result = service.handle(new GetAllAnswerQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(answerRepository, times(1)).findAll();
        verifyNoMoreInteractions(answerRepository);
    }

    @Test
    @DisplayName("handle(GetAnswerByIdQuery) -> returns Optional with Answer when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var answer = sample();
        when(answerRepository.findById(21L)).thenReturn(Optional.of(answer));

        // Act
        Optional<Answer> result = service.handle(new GetAnswerByIdQuery(21L));

        // Assert
        assertThat(result).isPresent().containsSame(answer);
        verify(answerRepository, times(1)).findById(21L);
        verifyNoMoreInteractions(answerRepository);
    }

    @Test
    @DisplayName("handle(GetAnswerByIdQuery) -> returns Optional.empty when no Answer found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(answerRepository.findById(21L)).thenReturn(Optional.empty());

        // Act
        Optional<Answer> result = service.handle(new GetAnswerByIdQuery(21L));

        // Assert
        assertThat(result).isEmpty();
        verify(answerRepository, times(1)).findById(21L);
        verifyNoMoreInteractions(answerRepository);
    }
}
