package pe.edu.upc.soft.work.platform.worker.forum.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Asset;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.AssetFactory;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllAssetsQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAssetByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.AssetRepository;
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
class AssetQueryServiceImplTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetQueryServiceImpl service;

    private static Asset sample() {
        var command = WorkerForumCommandFixtures.validCreateAssetCommand();
        return AssetFactory.create(
            command.messageId(),
            command.name(),
            command.url(),
            command.fileSize(),
            command.fileType()
        );
    }

    @Test
    @DisplayName("handle(GetAllAttachmentQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Asset> assets = List.of(sample(), sample());
        when(assetRepository.findAll()).thenReturn(assets);

        // Act
        List<Asset> result = service.handle(new GetAllAssetsQuery());

        // Assert
        assertThat(result).hasSize(2).containsExactlyElementsOf(assets);
        verify(assetRepository, times(1)).findAll();
        verifyNoMoreInteractions(assetRepository);
    }

    @Test
    @DisplayName("handle(GetAllAttachmentQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(assetRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Asset> result = service.handle(new GetAllAssetsQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(assetRepository, times(1)).findAll();
        verifyNoMoreInteractions(assetRepository);
    }

    @Test
    @DisplayName("handle(GetAttachmentByIdQuery) -> returns Optional with Attachment when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var attachment = sample();
        when(assetRepository.findById(31L)).thenReturn(Optional.of(attachment));

        // Act
        Optional<Asset> result = service.handle(new GetAssetByIdQuery(31L));

        // Assert
        assertThat(result).isPresent().containsSame(attachment);
        verify(assetRepository, times(1)).findById(31L);
        verifyNoMoreInteractions(assetRepository);
    }

    @Test
    @DisplayName("handle(GetAttachmentByIdQuery) -> returns Optional.empty when no Attachment found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(assetRepository.findById(31L)).thenReturn(Optional.empty());

        // Act
        Optional<Asset> result = service.handle(new GetAssetByIdQuery(31L));

        // Assert
        assertThat(result).isEmpty();
        verify(assetRepository, times(1)).findById(31L);
        verifyNoMoreInteractions(assetRepository);
    }
}
