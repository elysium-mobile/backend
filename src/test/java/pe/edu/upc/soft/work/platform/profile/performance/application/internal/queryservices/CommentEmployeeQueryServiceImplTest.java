package pe.edu.upc.soft.work.platform.profile.performance.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.CommentEmployee;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetAllCommentEmployeeQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetCommentEmployeeByIdQuery;
import pe.edu.upc.soft.work.platform.profile.performance.infrastructure.persistence.jpa.repositories.CommentEmployeeRepository;
import pe.edu.upc.soft.work.platform.profile.performance.test.fixtures.ProfilePerformanceCommandFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentEmployeeQueryServiceImplTest {

    @Mock
    private CommentEmployeeRepository commentemployeeRepository;

    @InjectMocks
    private CommentEmployeeQueryServiceImpl service;

    private static CommentEmployee sample() {
        return new CommentEmployee(ProfilePerformanceCommandFixtures.validCreateCommentEmployeeCommand());
    }

    @Test
    @DisplayName("handle(GetAllCommentEmployeeQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<CommentEmployee> comments = List.of(sample(), sample());
        when(commentemployeeRepository.findAll()).thenReturn(comments);

        // Act
        List<CommentEmployee> result = service.handle(new GetAllCommentEmployeeQuery());

        // Assert
        assertThat(result).hasSize(2).containsExactlyElementsOf(comments);
        verify(commentemployeeRepository, times(1)).findAll();
        verifyNoMoreInteractions(commentemployeeRepository);
    }

    @Test
    @DisplayName("handle(GetAllCommentEmployeeQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(commentemployeeRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<CommentEmployee> result = service.handle(new GetAllCommentEmployeeQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(commentemployeeRepository, times(1)).findAll();
        verifyNoMoreInteractions(commentemployeeRepository);
    }

    @Test
    @DisplayName("handle(GetCommentEmployeeByIdQuery) -> returns Optional with CommentEmployee when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var comment = sample();
        when(commentemployeeRepository.findById(13L)).thenReturn(Optional.of(comment));

        // Act
        Optional<CommentEmployee> result = service.handle(new GetCommentEmployeeByIdQuery(13L));

        // Assert
        assertThat(result).isPresent().containsSame(comment);
        verify(commentemployeeRepository, times(1)).findById(13L);
        verifyNoMoreInteractions(commentemployeeRepository);
    }

    @Test
    @DisplayName("handle(GetCommentEmployeeByIdQuery) -> returns Optional.empty when no CommentEmployee found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(commentemployeeRepository.findById(13L)).thenReturn(Optional.empty());

        // Act
        Optional<CommentEmployee> result = service.handle(new GetCommentEmployeeByIdQuery(13L));

        // Assert
        assertThat(result).isEmpty();
        verify(commentemployeeRepository, times(1)).findById(13L);
        verifyNoMoreInteractions(commentemployeeRepository);
    }
}
