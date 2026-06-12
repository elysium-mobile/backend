package pe.edu.upc.soft.work.platform.feedback.interfaces.acl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.feedback.domain.model.aggregates.Survey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyQueryService;
import pe.edu.upc.soft.work.platform.feedback.test.fixtures.FeedbackCommandFixtures;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackContextFacadeTest {

    @Mock
    private SurveyQueryService surveyQueryService;

    @InjectMocks
    private FeedbackContextFacade facade;

    @Test
    @DisplayName("existsSurveyById(Long) -> returns true when query service returns Optional with value (AAA)")
    void existsSurveyByIdPresent() {
        // Arrange
        var survey = new Survey(FeedbackCommandFixtures.validCreateSurveyCommand());
        when(surveyQueryService.handle(any(GetSurveyByIdQuery.class))).thenReturn(Optional.of(survey));

        // Act
        boolean result = facade.existsSurveyById(7L);

        // Assert
        assertThat(result).isTrue();
        verify(surveyQueryService, times(1)).handle(any(GetSurveyByIdQuery.class));
        verifyNoMoreInteractions(surveyQueryService);
    }

    @Test
    @DisplayName("existsSurveyById(Long) -> returns false when query service returns Optional.empty (AAA)")
    void existsSurveyByIdAbsent() {
        // Arrange
        when(surveyQueryService.handle(any(GetSurveyByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        boolean result = facade.existsSurveyById(7L);

        // Assert
        assertThat(result).isFalse();
        verify(surveyQueryService, times(1)).handle(any(GetSurveyByIdQuery.class));
        verifyNoMoreInteractions(surveyQueryService);
    }
}
