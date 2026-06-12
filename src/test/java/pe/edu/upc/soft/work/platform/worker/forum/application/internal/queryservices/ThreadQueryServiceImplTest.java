package pe.edu.upc.soft.work.platform.worker.forum.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllThreadQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetThreadByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ThreadRepository;
import pe.edu.upc.soft.work.platform.worker.forum.test.fixtures.WorkerForumCommandFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThreadQueryServiceImplTest {

    @Mock
    private ThreadRepository threadRepository;

    @InjectMocks
    private ThreadQueryServiceImpl service;

    private static Thread sample() {
        return new Thread(WorkerForumCommandFixtures.validCreateThreadCommand());
    }

    @Test
    @DisplayName("handle(GetAllThreadQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Thread> threads = List.of(sample());
        when(threadRepository.findAll()).thenReturn(threads);

        // Act
        List<Thread> result = service.handle(new GetAllThreadQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(threads);
        verify(threadRepository, times(1)).findAll();
        verifyNoMoreInteractions(threadRepository);
    }

    @Test
    @DisplayName("handle(GetAllThreadQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(threadRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Thread> result = service.handle(new GetAllThreadQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(threadRepository, times(1)).findAll();
        verifyNoMoreInteractions(threadRepository);
    }

    @Test
    @DisplayName("handle(GetThreadByIdQuery) -> returns Optional with Thread when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var thread = sample();
        when(threadRepository.findById(71L)).thenReturn(Optional.of(thread));

        // Act
        Optional<Thread> result = service.handle(new GetThreadByIdQuery(71L));

        // Assert
        assertThat(result).isPresent().containsSame(thread);
        verify(threadRepository, times(1)).findById(71L);
        verifyNoMoreInteractions(threadRepository);
    }

    @Test
    @DisplayName("handle(GetThreadByIdQuery) -> returns Optional.empty when no Thread found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(threadRepository.findById(71L)).thenReturn(Optional.empty());

        // Act
        Optional<Thread> result = service.handle(new GetThreadByIdQuery(71L));

        // Assert
        assertThat(result).isEmpty();
        verify(threadRepository, times(1)).findById(71L);
        verifyNoMoreInteractions(threadRepository);
    }
}
