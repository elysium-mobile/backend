package pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

@Entity
@DiscriminatorValue("VIDEO")
public class VideoAsset extends Asset {

  public VideoAsset(){}

  public VideoAsset(Long messageId, String name, String url, String fileSize){
    super(messageId, name, url, fileSize);
  }

  @Override
  public FileType getFileType() {
    return FileType.VIDEO;
  }

  @Override
  public boolean isViewable() {
    return true;
  }

  @Override
  public boolean isReadable() {
    return false;
  }

}
