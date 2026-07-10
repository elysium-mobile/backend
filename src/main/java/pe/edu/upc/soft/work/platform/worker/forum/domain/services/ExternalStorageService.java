package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

/**
 *  Service interface for handling file uploads and deletions to an external storage system (e.g., AWS S3, Google Cloud Storage).
 */
public interface ExternalStorageService {

  /**
   *  Uploads a file to the external storage system.
   * @param file  the file to be uploaded
   * @param fileType  the type of the file being uploaded (e.g., IMAGE, DOCUMENT)
   * @return  an UploadResult containing the public URL of the uploaded file and its size
   */
  UploadResult upload(MultipartFile file, FileType fileType);


  /**
   *  Deletes a file from the external storage system using its public URL.
   * @param publicUrl the public URL of the file to be deleted
   */
  void delete(String publicUrl);


  /**
   *  Value object representing the result of a file upload operation.
   * @param url the public URL of the uploaded file
   * @param fileSize  the size of the uploaded file in bytes
   */
  record UploadResult(String url, String fileSize) {}
}
