package pe.edu.upc.soft.work.platform.feedback.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.SurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllSurveyResponseQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyResponseByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.SurveyResponseRepository;
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
class SurveyResponseQueryServiceImplTest {

    @Mock
    private SurveyResponseRepository surveyresponseRepository;

    @InjectMocks
    private SurveyResponseQueryServiceImpl service;

    private static SurveyResponse sample() {
        return new SurveyResponse(FeedbackCommandFixtures.validCreateSurveyResponseCommand());
    }

    @Test
    @DisplayName("handle(GetAllSurveyResponseQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<SurveyResponse> responses = List.of(sample());
        when(surveyresponseRepository.findAll()).thenReturn(responses);

        // Act
        List<SurveyResponse> result = service.handle(new GetAllSurveyResponseQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(responses);
        verify(surveyresponseRepository, times(1)).findAll();
        verifyNoMoreInteractions(surveyresponseRepository);
    }

    @Test
    @DisplayName("handle(GetAllSurveyResponseQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(surveyresponseRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<SurveyResponse> result = service.handle(new GetAllSurveyResponseQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(surveyresponseRepository, times(1)).findAll();
        verifyNoMoreInteractions(surveyresponseRepository);
    }

    @Test
    @DisplayName("handle(GetSurveyResponseByIdQuery) -> returns Optional with SurveyResponse when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var sr = sample();
        when(surveyresponseRepository.findById(31L)).thenReturn(Optional.of(sr));

        // Act
        Optional<SurveyResponse> result = service.handle(new GetSurveyResponseByIdQuery(31L));

        // Assert
        assertThat(result).isPresent().containsSame(sr);
        verify(surveyresponseRepository, times(1)).findById(31L);
        verifyNoMoreInteractions(surveyresponseRepository);
    }

    @Test
    @DisplayName("handle(GetSurveyResponseByIdQuery) -> returns Optional.empty when no SurveyResponse found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(surveyresponseRepository.findById(31L)).thenReturn(Optional.empty());

        // Act
        Optional<SurveyResponse> result = service.handle(new GetSurveyResponseByIdQuery(31L));

        // Assert
        assertThat(result).isEmpty();
        verify(surveyresponseRepository, times(1)).findById(31L);
        verifyNoMoreInteractions(surveyresponseRepository);
    }
}
