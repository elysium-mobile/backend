package pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

import static org.hibernate.type.StandardBasicTypes.IMAGE;

public class AssetFactory {

  private AssetFactory(){}

  public static Asset create(Long messageId, String name, String url, String fileSize,
                             FileType fileType){
    return switch (fileType){
      case VIDEO -> new VideoAsset();
      case JPEG -> new ImageAsset();
      case PDF -> new PdfAsset();
    };
  }
}
