package pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

@Entity
@DiscriminatorValue("JPEG")
public class ImageAsset extends Asset{

  public ImageAsset(){}

  public ImageAsset(Long message, String name, String url, String fileSize){
    super(message, name, url, fileSize);
  }

  @Override
  public FileType getFileType() {
    return FileType.JPEG;
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
