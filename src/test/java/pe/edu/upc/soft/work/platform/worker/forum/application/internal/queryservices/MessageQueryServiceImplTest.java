package pe.edu.upc.soft.work.platform.worker.forum.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Message;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllMessageQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetMessageByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.MessageRepository;
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
class MessageQueryServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageQueryServiceImpl service;

    private static Message sample() {
        return new Message(WorkerForumCommandFixtures.validCreateMessageCommand());
    }

    @Test
    @DisplayName("handle(GetAllMessageQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Message> messages = List.of(sample());
        when(messageRepository.findAll()).thenReturn(messages);

        // Act
        List<Message> result = service.handle(new GetAllMessageQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(messages);
        verify(messageRepository, times(1)).findAll();
        verifyNoMoreInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(GetAllMessageQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(messageRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Message> result = service.handle(new GetAllMessageQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(messageRepository, times(1)).findAll();
        verifyNoMoreInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(GetMessageByIdQuery) -> returns Optional with Message when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var message = sample();
        when(messageRepository.findById(61L)).thenReturn(Optional.of(message));

        // Act
        Optional<Message> result = service.handle(new GetMessageByIdQuery(61L));

        // Assert
        assertThat(result).isPresent().containsSame(message);
        verify(messageRepository, times(1)).findById(61L);
        verifyNoMoreInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(GetMessageByIdQuery) -> returns Optional.empty when no Message found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(messageRepository.findById(61L)).thenReturn(Optional.empty());

        // Act
        Optional<Message> result = service.handle(new GetMessageByIdQuery(61L));

        // Assert
        assertThat(result).isEmpty();
        verify(messageRepository, times(1)).findById(61L);
        verifyNoMoreInteractions(messageRepository);
    }
}
