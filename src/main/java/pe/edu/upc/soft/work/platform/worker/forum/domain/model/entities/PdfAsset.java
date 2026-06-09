package pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

@Entity
@DiscriminatorValue("PDF")
public class PdfAsset extends Asset {

  public PdfAsset(){}

  public PdfAsset(Long messageId, String name, String url, String fileSize) {
    super(messageId, name, url, fileSize);
  }

  @Override
  public FileType getFileType() {
    return FileType.PDF;
  }

  @Override
  public boolean isViewable() {
    return false;
  }

  @Override
  public boolean isReadable() {
    return true;
  }
}
