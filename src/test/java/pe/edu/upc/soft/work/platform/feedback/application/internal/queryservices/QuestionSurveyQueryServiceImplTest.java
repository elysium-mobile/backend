package pe.edu.upc.soft.work.platform.feedback.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.QuestionSurvey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllQuestionSurveyQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetQuestionSurveyByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.QuestionSurveyRepository;
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
class QuestionSurveyQueryServiceImplTest {

    @Mock
    private QuestionSurveyRepository questionsurveyRepository;

    @InjectMocks
    private QuestionSurveyQueryServiceImpl service;

    private static QuestionSurvey sample() {
        return new QuestionSurvey(FeedbackCommandFixtures.validCreateQuestionSurveyCommand());
    }

    @Test
    @DisplayName("handle(GetAllQuestionSurveyQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<QuestionSurvey> questions = List.of(sample());
        when(questionsurveyRepository.findAll()).thenReturn(questions);

        // Act
        List<QuestionSurvey> result = service.handle(new GetAllQuestionSurveyQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(questions);
        verify(questionsurveyRepository, times(1)).findAll();
        verifyNoMoreInteractions(questionsurveyRepository);
    }

    @Test
    @DisplayName("handle(GetAllQuestionSurveyQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(questionsurveyRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<QuestionSurvey> result = service.handle(new GetAllQuestionSurveyQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(questionsurveyRepository, times(1)).findAll();
        verifyNoMoreInteractions(questionsurveyRepository);
    }

    @Test
    @DisplayName("handle(GetQuestionSurveyByIdQuery) -> returns Optional with QuestionSurvey when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var qs = sample();
        when(questionsurveyRepository.findById(14L)).thenReturn(Optional.of(qs));

        // Act
        Optional<QuestionSurvey> result = service.handle(new GetQuestionSurveyByIdQuery(14L));

        // Assert
        assertThat(result).isPresent().containsSame(qs);
        verify(questionsurveyRepository, times(1)).findById(14L);
        verifyNoMoreInteractions(questionsurveyRepository);
    }

    @Test
    @DisplayName("handle(GetQuestionSurveyByIdQuery) -> returns Optional.empty when no QuestionSurvey found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(questionsurveyRepository.findById(14L)).thenReturn(Optional.empty());

        // Act
        Optional<QuestionSurvey> result = service.handle(new GetQuestionSurveyByIdQuery(14L));

        // Assert
        assertThat(result).isEmpty();
        verify(questionsurveyRepository, times(1)).findById(14L);
        verifyNoMoreInteractions(questionsurveyRepository);
    }
}
