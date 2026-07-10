package pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

public class AssetFactory {

  private AssetFactory(){}

  public static Asset create(Long messageId, String name, String url, String fileSize,
                             FileType fileType){
    return switch (fileType){
      case VIDEO -> new VideoAsset(messageId,name,url,fileSize);
      case JPEG -> new ImageAsset(messageId,name,url,fileSize);
      case PDF -> new PdfAsset(messageId,name,url,fileSize);
    };
  }
}
