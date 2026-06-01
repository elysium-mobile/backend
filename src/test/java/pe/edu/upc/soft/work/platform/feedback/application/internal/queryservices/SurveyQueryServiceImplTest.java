package pe.edu.upc.soft.work.platform.feedback.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.feedback.domain.model.aggregates.Survey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllSurveyQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.SurveyRepository;
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
class SurveyQueryServiceImplTest {

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private SurveyQueryServiceImpl service;

    private static Survey sample() {
        return new Survey(FeedbackCommandFixtures.validCreateSurveyCommand());
    }

    @Test
    @DisplayName("handle(GetAllSurveyQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Survey> surveys = List.of(sample(), sample());
        when(surveyRepository.findAll()).thenReturn(surveys);

        // Act
        List<Survey> result = service.handle(new GetAllSurveyQuery());

        // Assert
        assertThat(result).hasSize(2).containsExactlyElementsOf(surveys);
        verify(surveyRepository, times(1)).findAll();
        verifyNoMoreInteractions(surveyRepository);
    }

    @Test
    @DisplayName("handle(GetAllSurveyQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(surveyRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Survey> result = service.handle(new GetAllSurveyQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(surveyRepository, times(1)).findAll();
        verifyNoMoreInteractions(surveyRepository);
    }

    @Test
    @DisplayName("handle(GetSurveyByIdQuery) -> returns Optional with Survey when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var survey = sample();
        when(surveyRepository.findById(7L)).thenReturn(Optional.of(survey));

        // Act
        Optional<Survey> result = service.handle(new GetSurveyByIdQuery(7L));

        // Assert
        assertThat(result).isPresent().containsSame(survey);
        verify(surveyRepository, times(1)).findById(7L);
        verifyNoMoreInteractions(surveyRepository);
    }

    @Test
    @DisplayName("handle(GetSurveyByIdQuery) -> returns Optional.empty when no Survey found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(surveyRepository.findById(7L)).thenReturn(Optional.empty());

        // Act
        Optional<Survey> result = service.handle(new GetSurveyByIdQuery(7L));

        // Assert
        assertThat(result).isEmpty();
        verify(surveyRepository, times(1)).findById(7L);
        verifyNoMoreInteractions(surveyRepository);
    }
}
