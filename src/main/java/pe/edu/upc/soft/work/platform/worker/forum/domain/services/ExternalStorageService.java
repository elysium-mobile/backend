package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

public interface ExternalStorageService {

  UploadResult upload(MultipartFile file, FileType fileType);


  void delete(String publicUrl);


  record UploadResult(String url, String fileSize) {}
}
